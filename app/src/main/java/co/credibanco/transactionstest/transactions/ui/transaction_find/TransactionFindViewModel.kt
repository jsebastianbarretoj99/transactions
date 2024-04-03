package co.credibanco.transactionstest.transactions.ui.transaction_find

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.credibanco.transactionstest.transactions.data.TransactionsRepository
import co.credibanco.transactionstest.transactions.model.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionFindViewModel(
    private val transactionsRepository: TransactionsRepository,
): ViewModel() {

    private var receiptIds: List<String> = emptyList()

    private val _transactionFindScreenData = MutableStateFlow<TransactionFindScreenData>(
        TransactionFindScreenData.FindTransactions(
            transactionFindData = TransactionFindData(
                query = "",
                active = false,
                receiptIds = receiptIds,
                goToDetail = false
            )
        )
    )

    val transactionFindScreenData = _transactionFindScreenData.asStateFlow()

    fun findReceiptId(query: String) {
        _transactionFindScreenData.value = TransactionFindScreenData.FindTransactions(
            transactionFindData = _transactionFindScreenData.value.transactionFindData.copy(
                query = query,
                receiptIds = receiptIds.filterReceiptIds(query)
            )
        )
    }

    fun onActiveChange(active: Boolean) {
        _transactionFindScreenData.value = TransactionFindScreenData.FindTransactions(
            transactionFindData = _transactionFindScreenData.value.transactionFindData.copy(
                active = active,
                query = "",
                receiptIds = receiptIds
            )
        )
    }

    fun onSearch(query: String) {
        if (receiptIds.contains(query)) {
            _transactionFindScreenData.value = TransactionFindScreenData.FindTransactions(
                transactionFindData = _transactionFindScreenData.value.transactionFindData.copy(
                    goToDetail = true,
                    query = query
                )
            )
        } else {
            _transactionFindScreenData.value = TransactionFindScreenData.Error(
                message = "Receipt ID not found",
                transactionFindData = _transactionFindScreenData.value.transactionFindData.copy(
                    query = query
                ),
            )
        }
    }

    fun updateGoToDetail() {
        _transactionFindScreenData.value = TransactionFindScreenData.FindTransactions(
            transactionFindData = _transactionFindScreenData.value.transactionFindData.copy(
                goToDetail = false,
                query = "",
                receiptIds = receiptIds,
                active = false
            )
        )
    }

    fun getReceiptIdAllTransactions() {
        viewModelScope.launch {
            transactionsRepository.getReceiptIdAllTransactions().let { response ->
                receiptIds = when (response) {
                    is Response.Success -> {
                        response.data ?: emptyList()
                    }
                    is Response.Failure -> {
                        emptyList()
                    }
                }
                _transactionFindScreenData.value = TransactionFindScreenData.FindTransactions(
                    transactionFindData = _transactionFindScreenData.value.transactionFindData.copy(
                        receiptIds = receiptIds
                    )
                )
            }
        }
    }

    private fun List<String>.filterReceiptIds(query: String): List<String> {
        return filter { it.contains(query, ignoreCase = true) }
    }

}
