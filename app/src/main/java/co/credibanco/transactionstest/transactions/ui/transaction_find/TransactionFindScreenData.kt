package co.credibanco.transactionstest.transactions.ui.transaction_find

data class TransactionFindData(
    val query: String,
    val active: Boolean,
    val receiptIds: List<String>,
    val goToDetail: Boolean,
)

sealed class TransactionFindScreenData(
    open val transactionFindData: TransactionFindData,
) {
    data class FindTransactions(
        override val transactionFindData: TransactionFindData,
    ) : TransactionFindScreenData(
        transactionFindData
    )

    data class Error(
        val message: String,
        override val transactionFindData: TransactionFindData,
    ) : TransactionFindScreenData(
        transactionFindData
    )
}
