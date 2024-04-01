package co.credibanco.transactionstest.transactions.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.credibanco.transactionstest.transactions.model.Transaction

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val commerceCode: String,
    val terminalCode: String,
    val amount: String,
    val card: String,
    val receiptId: String = "",
    val rrn: String = "",
    val status: TransactionStatusEntity = TransactionStatusEntity.PENDING,
)

fun TransactionEntity.toTransaction() = Transaction(
    id = id,
    commerceCode = commerceCode,
    terminalCode = terminalCode,
    amount = amount,
    card = card,
    receiptId = receiptId,
    rrn = rrn,
    status = status.toTransactionStatus(),
)
