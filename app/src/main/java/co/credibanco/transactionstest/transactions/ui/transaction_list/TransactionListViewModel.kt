package co.credibanco.transactionstest.transactions.ui.transaction_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.credibanco.transactionstest.transactions.data.TransactionsRepository
import co.credibanco.transactionstest.transactions.model.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val transactionsRepository: TransactionsRepository,
): ViewModel() {

    private val _transactionListScreenData = MutableStateFlow<TransactionListScreenData>(
        TransactionListScreenData.Loading
    )

    val transactionListScreenData = _transactionListScreenData.asStateFlow()

    fun getTransactions() {
        viewModelScope.launch {
            _transactionListScreenData.value = TransactionListScreenData.Loading
            transactionsRepository.getTransactions().let { response ->
                _transactionListScreenData.value = when (response) {
                    is Response.Success -> {
                        if (response.data != null) {
                            if (response.data.isEmpty()) {
                                TransactionListScreenData.Empty(NOT_FOUND_TRANSACTION)
                            } else {
                                TransactionListScreenData.Transactions(response.data)
                            }
                        } else {
                            TransactionListScreenData.Error(ERROR_GETTING_TRANSACTIONS)
                        }
                    }
                    is Response.Failure -> {
                        TransactionListScreenData.Error(
                            response.error?.message ?: ERROR_GETTING_TRANSACTIONS
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val NOT_FOUND_TRANSACTION = "Transacciones no encontradas"
        const val ERROR_GETTING_TRANSACTIONS = "Error al obtener transacciones"
    }
}
