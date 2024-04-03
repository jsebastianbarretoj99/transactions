package co.credibanco.transactionstest.transactions.ui.transaction_authorization

import co.credibanco.transactionstest.transactions.model.Transaction

sealed class TransactionAuthorizationScreenData(
    open val message: String = "",
    open val transaction: Transaction,
) {
    data class TransactionFormData(
        override val transaction: Transaction
    ) : TransactionAuthorizationScreenData(transaction = transaction)

    data class TransactionAuthorized(
        override val message: String,
        override val transaction: Transaction,
        val showToast: Boolean
    ) : TransactionAuthorizationScreenData(message, transaction)

    data class Error(
        override val message: String,
        override val transaction: Transaction,
        val showToast: Boolean
    ) : TransactionAuthorizationScreenData(message, transaction)

    data class Loading(
        override val transaction: Transaction
    ) : TransactionAuthorizationScreenData(transaction = transaction)
}
