package co.credibanco.transactionstest.transactions.data.local.model

import co.credibanco.transactionstest.transactions.model.TransactionStatus

enum class TransactionStatusEntity {
    PENDING,
    APPROVED,
    ANNULLED,
}

fun TransactionStatusEntity.toTransactionStatus() = when (this) {
    TransactionStatusEntity.PENDING -> TransactionStatus.PENDING
    TransactionStatusEntity.APPROVED -> TransactionStatus.APPROVED
    TransactionStatusEntity.ANNULLED -> TransactionStatus.ANNULLED
}
