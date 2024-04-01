package co.credibanco.transactionstest.transactions.data

import co.credibanco.transactionstest.transactions.data.local.TransactionDao
import co.credibanco.transactionstest.transactions.data.local.model.TransactionStatusEntity
import co.credibanco.transactionstest.transactions.data.local.model.toTransaction
import co.credibanco.transactionstest.transactions.data.remote.TransactionsService
import co.credibanco.transactionstest.transactions.model.Response
import co.credibanco.transactionstest.transactions.model.Transaction
import co.credibanco.transactionstest.transactions.model.annul
import co.credibanco.transactionstest.transactions.model.authorize
import co.credibanco.transactionstest.transactions.model.toAnnulmentRequest
import co.credibanco.transactionstest.transactions.model.toAuthorizationRequest
import co.credibanco.transactionstest.transactions.model.toTransactionEntity

interface TransactionsRepository {
    suspend fun postAuthorization(transaction: Transaction): Response<Transaction>
    suspend fun postAnnulment(transaction: Transaction): Response<Transaction>
    suspend fun findTransactionByReceiptId(receiptId: String): Response<Transaction>
    suspend fun getTransactions(): Response<List<Transaction>>
}

class TransactionsRepositoryImpl(
    private val transactionsDao: TransactionDao,
    private val transactionsService: TransactionsService,
): TransactionsRepository {
    override suspend fun postAuthorization(transaction: Transaction): Response<Transaction> {
        val transactionDb = transactionsDao.findTransactionById(
            id = transaction.id
        )

        if (transactionDb != null) {
            return Response.Failure(Exception(TRANSACTION_ALREADY_AUTHORIZED))
        }

        val response = transactionsService.postAuthorization(
            authorizationRequest = transaction.toAuthorizationRequest()
        )
        val authorizationResult = response.body()
        return if (response.isSuccessful && authorizationResult != null) {
            val transactionAuthorized = transaction.authorize(
                receiptId = authorizationResult.receiptId,
                rrn = authorizationResult.rrn
            )

            transactionsDao.insertTransaction(
                transactionAuthorized.toTransactionEntity()
            )

            Response.Success(transactionAuthorized)
        } else {
            Response.Failure(Exception(ERROR_POST_AUTHORIZATION))
        }
    }

    override suspend fun postAnnulment(transaction: Transaction): Response<Transaction> {
        val transactionDb = transactionsDao.findTransactionByReceiptId(
            receiptId = transaction.receiptId
        ) ?: return Response.Failure(Exception(TRANSACTION_NOT_FOUND))

        if(transactionDb.status == TransactionStatusEntity.ANNULLED) {
            return Response.Failure(Exception(TRANSACTION_ALREADY_ANNULLED))
        }

        val response = transactionsService.postAnnulment(
            annulmentRequest = transaction.toAnnulmentRequest()
        )
        return if (response.isSuccessful) {
            val transactionAnnulled = transaction.annul()
            transactionsDao.updateTransaction(
                transactionAnnulled.toTransactionEntity()
            )
            Response.Success(transactionAnnulled)
        } else {
            Response.Failure(Exception(ERROR_POST_ANNULMENT))
        }
    }

    override suspend fun findTransactionByReceiptId(receiptId: String): Response<Transaction> {
        val transaction = transactionsDao.findTransactionByReceiptId(receiptId)
        return if (transaction != null) {
            Response.Success(transaction.toTransaction())
        } else {
            Response.Failure(Exception(TRANSACTION_NOT_FOUND))
        }
    }

    override suspend fun getTransactions(): Response<List<Transaction>> {
        val transactions = transactionsDao.getTransactions().map { it.toTransaction() }
        return Response.Success(transactions)
    }

    companion object {
        const val ERROR_POST_AUTHORIZATION = "Failed to post authorization"
        const val ERROR_POST_ANNULMENT = "Failed to post annulment"
        const val TRANSACTION_ALREADY_AUTHORIZED = "Transaction already authorized"
        const val TRANSACTION_NOT_FOUND = "Transaction not found"
        const val TRANSACTION_ALREADY_ANNULLED = "Transaction already annulled"
    }
}
