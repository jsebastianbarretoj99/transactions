package co.credibanco.transactionstest.transactions.data

import co.credibanco.transactionstest.providers.DispatcherProvider
import co.credibanco.transactionstest.transactions.data.local.TransactionDao
import co.credibanco.transactionstest.transactions.data.local.model.TransactionStatusEntity
import co.credibanco.transactionstest.transactions.data.local.model.toTransaction
import co.credibanco.transactionstest.transactions.data.remote.TransactionsService
import co.credibanco.transactionstest.transactions.data.remote.model.authorization.AuthorizationResult
import co.credibanco.transactionstest.transactions.model.Response
import co.credibanco.transactionstest.transactions.model.Transaction
import co.credibanco.transactionstest.transactions.model.annul
import co.credibanco.transactionstest.transactions.model.authorize
import co.credibanco.transactionstest.transactions.model.toAnnulmentRequest
import co.credibanco.transactionstest.transactions.model.toAuthorizationRequest
import co.credibanco.transactionstest.transactions.model.toTransactionEntity
import com.google.gson.GsonBuilder
import kotlinx.coroutines.withContext

interface TransactionsRepository {
    suspend fun postAuthorization(transaction: Transaction): Response<Transaction>

    suspend fun postAnnulment(transaction: Transaction): Response<Transaction>

    suspend fun findTransactionByReceiptId(receiptId: String): Response<Transaction>

    suspend fun getTransactions(): Response<List<Transaction>>

    suspend fun getReceiptIdAllTransactions(): Response<List<String>>
}

class TransactionsRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val transactionsDao: TransactionDao,
    private val transactionsService: TransactionsService,
): TransactionsRepository {
    override suspend fun postAuthorization(transaction: Transaction): Response<Transaction> =
        withContext(dispatcherProvider.getIO()) {
            try {
                val transactionDb = transactionsDao.findTransactionById(
                    id = transaction.id
                )

                if (transactionDb != null) {
                    return@withContext Response.Failure(Exception(TRANSACTION_ALREADY_AUTHORIZED))
                }

                val response = transactionsService.postAuthorization(
                    authorizationRequest = transaction.toAuthorizationRequest()
                )
                val authorizationResult = response.body()
                return@withContext if (response.isSuccessful && authorizationResult != null) {
                    val transactionAuthorized = transaction.authorize(
                        receiptId = authorizationResult.receiptId,
                        rrn = authorizationResult.rrn
                    )

                    transactionsDao.insertTransaction(
                        transactionAuthorized.toTransactionEntity()
                    )

                    Response.Success(transactionAuthorized)
                } else {
                    val gson = GsonBuilder().create()
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        val errorData = gson.fromJson(errorBody.charStream(), AuthorizationResult::class.java)
                        Response.Failure(Exception("${errorData.statusCode} - ${errorData.statusDescription}"))
                    } else {
                        Response.Failure(Exception(ERROR_POST_AUTHORIZATION))
                    }
                }
            } catch (e: Exception) {
                Response.Failure(e)
            }
        }

    override suspend fun postAnnulment(transaction: Transaction): Response<Transaction> =
        withContext(dispatcherProvider.getIO()) {
            val transactionDb = transactionsDao.findTransactionByReceiptId(
                receiptId = transaction.receiptId
            ) ?: return@withContext Response.Failure(Exception(TRANSACTION_NOT_FOUND))

            if (transactionDb.status == TransactionStatusEntity.ANNULLED) {
                return@withContext Response.Failure(Exception(TRANSACTION_ALREADY_ANNULLED))
            }

            val response = transactionsService.postAnnulment(
                annulmentRequest = transaction.toAnnulmentRequest()
            )
            return@withContext if (response.isSuccessful) {
                val transactionAnnulled = transaction.annul()
                transactionsDao.updateTransaction(
                    transactionAnnulled.toTransactionEntity()
                )
                Response.Success(transactionAnnulled)
            } else {
                Response.Failure(Exception(ERROR_POST_ANNULMENT))
            }
        }

    override suspend fun findTransactionByReceiptId(receiptId: String): Response<Transaction> =
        withContext(dispatcherProvider.getIO()) {
            val transaction = transactionsDao.findTransactionByReceiptId(receiptId)
            return@withContext if (transaction != null) {
                Response.Success(transaction.toTransaction())
            } else {
                Response.Failure(Exception(TRANSACTION_NOT_FOUND))
            }
        }

    override suspend fun getTransactions(): Response<List<Transaction>> = withContext(
        dispatcherProvider.getIO()
    ) {
        return@withContext Response.Success(
            transactionsDao.getTransactions().map { it.toTransaction() }
        )
    }

    override suspend fun getReceiptIdAllTransactions(): Response<List<String>> = withContext(
        dispatcherProvider.getIO()
    ) {
        return@withContext Response.Success(transactionsDao.getReceiptIdAllTransactions())
    }

    companion object {
        const val ERROR_POST_AUTHORIZATION = "No se pudo publicar la autorización"
        const val ERROR_POST_ANNULMENT = "No se pudo publicar la anulación"
        const val TRANSACTION_ALREADY_AUTHORIZED = "Transacción ya autorizada"
        const val TRANSACTION_NOT_FOUND = "Transacción no encontrada"
        const val TRANSACTION_ALREADY_ANNULLED = "Transacción ya anulada"
    }
}
