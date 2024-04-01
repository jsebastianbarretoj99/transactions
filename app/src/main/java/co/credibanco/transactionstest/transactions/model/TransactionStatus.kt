package co.credibanco.transactionstest.transactions.model

import co.credibanco.transactionstest.transactions.data.local.model.TransactionStatusEntity

enum class TransactionStatus {
    PENDING,
    APPROVED,
    ANNULLED,
}

fun TransactionStatus.toTransactionStatusEntity() = when (this) {
    TransactionStatus.PENDING -> TransactionStatusEntity.PENDING
    TransactionStatus.APPROVED -> TransactionStatusEntity.APPROVED
    TransactionStatus.ANNULLED -> TransactionStatusEntity.ANNULLED
}
