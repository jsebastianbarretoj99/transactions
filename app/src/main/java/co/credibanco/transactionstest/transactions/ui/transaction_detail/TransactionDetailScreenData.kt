package co.credibanco.transactionstest.transactions.ui.transaction_detail

import co.credibanco.transactionstest.transactions.model.Transaction


sealed class TransactionDetailAnnulled(
    open val showToast: Boolean = false,
    open val message: String = "",
) {
    data class Success(
        override val message: String,
    ) : TransactionDetailAnnulled(
        showToast = true,
        message = message
    )

    data class Error(
        override val message: String,
    ) : TransactionDetailAnnulled(
        showToast = true,
        message = message
    )

    data object Loading : TransactionDetailAnnulled()

    data object Idle : TransactionDetailAnnulled()
}

sealed class TransactionDetailScreenData {
    data class TransactionDetail(
        val transaction: Transaction,
        val transactionAnnulled: TransactionDetailAnnulled
    ) : TransactionDetailScreenData()

    data class Error(
        val message: String,
    ) : TransactionDetailScreenData()

    data object Loading: TransactionDetailScreenData()
}
