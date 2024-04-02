package co.credibanco.transactionstest.transactions.ui.transaction_list

import co.credibanco.transactionstest.transactions.model.Transaction

sealed class TransactionListScreenData(
    open val message: String = "",
) {
    data class Transactions(
        val transactions: List<Transaction>,
    ) : TransactionListScreenData()

    data class Empty(
        override val message: String,
    ) : TransactionListScreenData()

    data class Error(
        override val message: String,
    ) : TransactionListScreenData()

    data object Loading : TransactionListScreenData()
}
