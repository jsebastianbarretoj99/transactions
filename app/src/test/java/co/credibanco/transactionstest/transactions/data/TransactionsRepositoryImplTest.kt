package co.credibanco.transactionstest.transactions.data

import co.credibanco.transactionstest.BaseTest
import co.credibanco.transactionstest.providers.DispatcherProvider
import co.credibanco.transactionstest.providers.TestDispatcherProvider
import co.credibanco.transactionstest.transactions.data.local.TransactionDao
import co.credibanco.transactionstest.transactions.data.remote.TransactionsService
import co.credibanco.transactionstest.transactions.data.remote.model.anullment.AnnulmentResult
import co.credibanco.transactionstest.transactions.data.remote.model.authorization.AuthorizationResult
import co.credibanco.transactionstest.transactions.model.Transaction
import co.credibanco.transactionstest.transactions.model.TransactionStatus
import co.credibanco.transactionstest.transactions.model.annul
import co.credibanco.transactionstest.transactions.model.toAnnulmentRequest
import co.credibanco.transactionstest.transactions.model.toAuthorizationRequest
import co.credibanco.transactionstest.transactions.model.toTransactionEntity
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import retrofit2.Response
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TransactionsRepositoryImplTest : BaseTest() {

    @RelaxedMockK
    private lateinit var transactionsServiceMock: TransactionsService

    @RelaxedMockK
    private lateinit var transactionsDaoMock: TransactionDao

    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    private lateinit var transactionsRepository: TransactionsRepository

    @BeforeTest
    override fun setUp() {
        super.setUp()
        MockKAnnotations.init(this)

        transactionsRepository = TransactionsRepositoryImpl(
            dispatcherProvider = dispatcherProvider,
            transactionsDao = transactionsDaoMock,
            transactionsService = transactionsServiceMock,
        )
    }

    @AfterTest
    override fun tearDown() {
        super.tearDown()
        clearAllMocks()
    }

    @Test
    fun `test postAuthorization when transaction is already authorized should return failure response`() = runTest {
        // GIVEN
        coEvery {
            transactionsDaoMock.findTransactionById(id = transaction.id)
        } returns transaction.toTransactionEntity()

        // WHEN
        val response = transactionsRepository.postAuthorization(transaction)

        // THEN
        assertEquals(
            expected =TRANSACTION_ALREADY_AUTHORIZED,
            actual = response.error?.message
        )
    }

    @Test
    fun `test postAuthorization when transaction is not authorized should return success response and call insertTransaction`() = runTest {
        // GIVEN
        coEvery {
            transactionsDaoMock.findTransactionById(id = transaction.id)
        } returns null

        coEvery {
            transactionsServiceMock.postAuthorization(
                authorizationRequest = transaction.toAuthorizationRequest()
            )
        } returns mockk<Response<AuthorizationResult>>().apply {
            coEvery { isSuccessful } returns true
            coEvery { body() } returns AuthorizationResult(
                receiptId = "77efbc3a-f22f-4d8d-92e4-64473e9d43bf",
                rrn = "b6321399-815f-427f-8593-12d31d0dc749",
                statusCode = "00",
                statusDescription = "Aprobada"
            )
        }

        // WHEN
        val response = transactionsRepository.postAuthorization(transaction)

        // THEN
        val expectedTransaction = transaction.copy(
            receiptId = RECEIPT_ID,
            rrn = RRN,
            status = TransactionStatus.APPROVED
        )
        assertEquals(
            expected = expectedTransaction,
            actual = response.data
        )
        coVerify {
            transactionsDaoMock.insertTransaction(expectedTransaction.toTransactionEntity())
        }
    }

    @Test
    fun `test postAuthorization when transaction is not authorized should return failure response and not call insertTransaction`() = runTest {
        // GIVEN
        coEvery {
            transactionsDaoMock.findTransactionById(id = transaction.id)
        } returns null

        coEvery {
            transactionsServiceMock.postAuthorization(
                authorizationRequest = transaction.toAuthorizationRequest()
            )
        } returns mockk<Response<AuthorizationResult>>().apply {
            coEvery { isSuccessful } returns false
            coEvery { body() } returns null
            coEvery { errorBody() } returns mockk(relaxed = true) {
                coEvery { charStream() } returns "{'receiptId':null,'rrn':null,'statusCode':'99','statusDescription':'Número de tarjeta incorrecto'}".reader()
            }
        }

        // WHEN
        val response = transactionsRepository.postAuthorization(transaction)

        // THEN
        assertEquals(
            expected = ERROR_POST_AUTHORIZATION,
            actual = response.error?.message
        )
        coVerify(exactly = 0) {
            transactionsDaoMock.insertTransaction(any())
        }
    }

    @Test
    fun `test postAnnulment when transaction is not found should return failure response`() = runTest {
        // GIVEN
        coEvery {
            transactionsDaoMock.findTransactionByReceiptId(receiptId = transaction.receiptId)
        } returns null

        // WHEN
        val response = transactionsRepository.postAnnulment(transaction)

        // THEN
        assertEquals(
            expected = TRANSACTION_NOT_FOUND,
            actual = response.error?.message
        )
    }

    @Test
    fun `test postAnnulment when transaction is already annulled should return failure response`() = runTest {
        // GIVEN
        coEvery {
            transactionsDaoMock.findTransactionByReceiptId(receiptId = transaction.receiptId)
        } returns transaction.copy(
            status = TransactionStatus.ANNULLED
        ).toTransactionEntity()

        // WHEN
        val response = transactionsRepository.postAnnulment(transaction)

        // THEN
        assertEquals(
            expected = TRANSACTION_ALREADY_ANNULLED,
            actual = response.error?.message
        )
    }

    @Test
    fun `test postAnnulment when transaction is not annulled should return success response and call updateTransaction`() = runTest {
        // GIVEN
        coEvery {
            transactionsDaoMock.findTransactionByReceiptId(receiptId = transaction.receiptId)
        } returns transaction.toTransactionEntity()

        coEvery {
            transactionsServiceMock.postAnnulment(
                annulmentRequest = transaction.toAnnulmentRequest()
            )
        } returns mockk<Response<AnnulmentResult>>().apply {
            coEvery { isSuccessful } returns true
        }

        // WHEN
        val response = transactionsRepository.postAnnulment(transaction)

        // THEN
        val expectedTransaction = transaction.annul()
        assertEquals(
            expected = expectedTransaction,
            actual = response.data
        )
        coVerify {
            transactionsDaoMock.updateTransaction(expectedTransaction.toTransactionEntity())
        }
    }

    @Test
    fun `test postAnnulment when transaction is not annulled should return failure response and not call updateTransaction`() = runTest {
        // GIVEN
        coEvery {
            transactionsDaoMock.findTransactionByReceiptId(receiptId = transaction.receiptId)
        } returns transaction.toTransactionEntity()

        coEvery {
            transactionsServiceMock.postAnnulment(
                annulmentRequest = transaction.toAnnulmentRequest()
            )
        } returns mockk<Response<AnnulmentResult>>().apply {
            coEvery { isSuccessful } returns false
            coEvery { errorBody() } returns null
        }

        // WHEN
        val response = transactionsRepository.postAnnulment(transaction)

        // THEN
        assertEquals(
            expected = ERROR_POST_ANNULMENT,
            actual = response.error?.message
        )
        coVerify(exactly = 0) {
            transactionsDaoMock.updateTransaction(any())
        }
    }

    @Test
    fun `test findTransactionByReceiptId should return transaction`() = runTest {
        // GIVEN
        coEvery {
            transactionsDaoMock.findTransactionByReceiptId(receiptId = RECEIPT_ID)
        } returns transaction.toTransactionEntity()

        // WHEN
        val result = transactionsRepository.findTransactionByReceiptId(RECEIPT_ID)

        // THEN
        assertEquals(
            expected = transaction,
            actual = result.data
        )
    }

    @Test
    fun `test findTransactionByReceiptId should return response failure`() = runTest {
        // GIVEN
        coEvery {
            transactionsDaoMock.findTransactionByReceiptId(receiptId = RECEIPT_ID)
        } returns null

        // WHEN
        val result = transactionsRepository.findTransactionByReceiptId(RECEIPT_ID)

        // THEN
        assertEquals(
            expected = TRANSACTION_NOT_FOUND,
            actual = result.error?.message
        )
    }

    @Test
    fun`test getTransactions should return list of transactions`() = runTest {
        // GIVEN
        val transactions = listOf(transaction)
        coEvery {
            transactionsDaoMock.getTransactions()
        } returns transactions.map { it.toTransactionEntity() }

        // WHEN
        val result = transactionsRepository.getTransactions()

        // THEN
        assertEquals(
            expected = transactions,
            actual = result.data
        )
    }

    @Test
    fun `test getReceiptIdAllTransactions should return list of receipt ids`() = runTest {
        // GIVEN
        val receiptIds = listOf("1", "2", "3")
        coEvery {
            transactionsDaoMock.getReceiptIdAllTransactions()
        } returns receiptIds

        // WHEN
        val result = transactionsRepository.getReceiptIdAllTransactions()

        // THEN
        assertEquals(
            expected = receiptIds,
            actual = result.data
        )
    }

    companion object {
        const val TRANSACTION_ALREADY_AUTHORIZED = "Transacción ya autorizada"
        const val TRANSACTION_NOT_FOUND = "Transacción no encontrada"
        const val TRANSACTION_ALREADY_ANNULLED = "Transacción ya anulada"
        const val ERROR_POST_ANNULMENT = "No se pudo publicar la anulación"
        const val ERROR_POST_AUTHORIZATION = "99 - Número de tarjeta incorrecto"
        const val RECEIPT_ID = "77efbc3a-f22f-4d8d-92e4-64473e9d43bf"
        const val RRN = "b6321399-815f-427f-8593-12d31d0dc749"
        val transaction = Transaction(
            id = "1",
            commerceCode = "000123",
            terminalCode = "000ABC",
            amount = "100.0",
            card = "1234567890123456",
            status = TransactionStatus.PENDING
        )
    }
}
