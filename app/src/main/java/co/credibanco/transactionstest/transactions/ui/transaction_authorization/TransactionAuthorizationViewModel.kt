package co.credibanco.transactionstest.transactions.ui.transaction_authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.credibanco.transactionstest.datastore.repository.DataStoreRepository
import co.credibanco.transactionstest.transactions.data.TransactionsRepository
import co.credibanco.transactionstest.transactions.model.Response
import co.credibanco.transactionstest.transactions.model.Transaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionAuthorizationViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val transactionsRepository: TransactionsRepository,
): ViewModel() {

    private val _transactionAuthorizationScreenData = MutableStateFlow<TransactionAuthorizationScreenData>(
        TransactionAuthorizationScreenData.TransactionFormData(
            transaction = Transaction(
                id = "",
                commerceCode = "",
                terminalCode = "",
                amount = "",
                card = "",
            )
        )
    )

    val transactionAuthorizationScreenData = _transactionAuthorizationScreenData.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreRepository.readData(COMMERCE_CODE).collect { response ->
                when(response) {
                    is Response.Success -> {
                        _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.TransactionFormData(
                            transaction = _transactionAuthorizationScreenData.value.transaction.copy(
                                commerceCode = response.data ?: ""
                            )
                        )
                    }

                    is Response.Failure -> {}
                }
            }
        }

        viewModelScope.launch {
            dataStoreRepository.readData(TERMINAL_CODE).collect { response ->
                when(response) {
                    is Response.Success -> {
                        _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.TransactionFormData(
                            transaction = _transactionAuthorizationScreenData.value.transaction.copy(
                                terminalCode = response.data ?: ""
                            )
                        )
                    }

                    is Response.Failure -> {}
                }
            }
        }
    }

    fun postTransactionAuthorization() {
        viewModelScope.launch {
            var transaction = _transactionAuthorizationScreenData.value.transaction

            _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.Loading(
                transaction = transaction
            )

            if (transaction.id.isEmpty() || transaction.amount.isEmpty() || transaction.card.isEmpty()) {
                _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.Error(
                    message = "Error: Todos los campos son requeridos",
                    transaction = transaction,
                    showToast = true,
                )
                delay(200)
                _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.TransactionFormData(
                    transaction = transaction
                )
            } else {
                transactionsRepository.postAuthorization(
                    transaction = transaction
                ).let { response ->
                    _transactionAuthorizationScreenData.value = when(response) {
                        is Response.Success -> {
                            transaction = response.data ?: transaction
                            TransactionAuthorizationScreenData.TransactionAuthorized(
                                message = "Transaction authorized with receipt ${transaction.receiptId} " +
                                        "and RRN ${transaction.rrn}",
                                transaction = transaction,
                                showToast = true,
                            )
                        }

                        is Response.Failure -> {
                            TransactionAuthorizationScreenData.Error(
                                message = "Error: ${response.error?.message ?: "Failed to authorize transaction"}",
                                transaction = transaction,
                                showToast = true,
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateTransactionId(id: String) {
        _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.TransactionFormData(
            transaction = _transactionAuthorizationScreenData.value.transaction.copy(
                id = id
            )
        )
    }

    fun updateAmountTransaction(amount: String) {
        _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.TransactionFormData(
            transaction = _transactionAuthorizationScreenData.value.transaction.copy(
                amount = amount
            )
        )
    }

    fun updateCardTransaction(card: String) {
        _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.TransactionFormData(
            transaction = _transactionAuthorizationScreenData.value.transaction.copy(
                card = card
            )
        )
    }

    fun updateShowToast() {
        if (_transactionAuthorizationScreenData.value is TransactionAuthorizationScreenData.TransactionAuthorized) {
            _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.TransactionAuthorized(
                message = "",
                transaction = _transactionAuthorizationScreenData.value.transaction,
                showToast = false
            )
        } else if (_transactionAuthorizationScreenData.value is TransactionAuthorizationScreenData.Error) {
            _transactionAuthorizationScreenData.value = TransactionAuthorizationScreenData.Error(
                message = "",
                transaction = _transactionAuthorizationScreenData.value.transaction,
                showToast = false
            )
        }
    }

    companion object {
        const val COMMERCE_CODE = "commerceCode"
        const val TERMINAL_CODE = "terminalCode"
    }
}
