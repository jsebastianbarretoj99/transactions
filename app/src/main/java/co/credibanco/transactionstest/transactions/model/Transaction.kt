package co.credibanco.transactionstest.transactions.model

import co.credibanco.transactionstest.transactions.data.local.model.TransactionEntity
import co.credibanco.transactionstest.transactions.data.remote.model.anullment.AnnulmentRequest
import co.credibanco.transactionstest.transactions.data.remote.model.authorization.AuthorizationRequest

data class Transaction(
    val id: String,
    val commerceCode: String,
    val terminalCode: String,
    val amount: String,
    val card: String,
    val receiptId: String = "",
    val rrn: String = "",
    val status: TransactionStatus = TransactionStatus.PENDING,
)

fun Transaction.toTransactionEntity() = TransactionEntity(
    id = id,
    commerceCode = commerceCode,
    terminalCode = terminalCode,
    amount = amount,
    card = card,
    receiptId = receiptId,
    rrn = rrn,
    status = status.toTransactionStatusEntity(),
)

fun Transaction.toAuthorizationRequest() = AuthorizationRequest(
    id = id,
    commerceCode = commerceCode,
    terminalCode = terminalCode,
    amount = amount,
    card = card,
)

fun Transaction.toAnnulmentRequest() = AnnulmentRequest(
    receiptId = receiptId,
    rrn = rrn,
)

fun Transaction.authorize(receiptId: String, rrn: String) = copy(
    receiptId = receiptId,
    rrn = rrn,
    status = TransactionStatus.APPROVED,
)

fun Transaction.annul() = copy(
    status = TransactionStatus.ANNULLED,
)
