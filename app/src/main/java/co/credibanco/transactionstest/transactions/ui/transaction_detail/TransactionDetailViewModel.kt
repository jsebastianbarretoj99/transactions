package co.credibanco.transactionstest.transactions.ui.transaction_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.credibanco.transactionstest.transactions.data.TransactionsRepository
import co.credibanco.transactionstest.transactions.model.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    private val transactionsRepository: TransactionsRepository,
) : ViewModel() {

    private val _transactionDetailScreenData = MutableStateFlow<TransactionDetailScreenData>(
        TransactionDetailScreenData.Loading
    )

    val transactionDetailScreenData = _transactionDetailScreenData.asStateFlow()


    fun getTransactionDetail(receiptId: String?) {
        receiptId?.let {
            viewModelScope.launch {
                _transactionDetailScreenData.value = TransactionDetailScreenData.Loading
                transactionsRepository.findTransactionByReceiptId(
                    receiptId = receiptId
                ).let { response ->
                    when(response) {
                        is Response.Success -> {
                            if (response.data != null) {
                                _transactionDetailScreenData.value = TransactionDetailScreenData.TransactionDetail(
                                    transaction = response.data,
                                    transactionAnnulled = TransactionDetailAnnulled.Idle,
                                )
                            } else {
                                _transactionDetailScreenData.value = TransactionDetailScreenData.Error(
                                    message = TRANSACTION_NOT_FOUND_ERROR_MESSAGE,
                                )
                            }
                        }
                        is Response.Failure -> {
                            _transactionDetailScreenData.value = TransactionDetailScreenData.Error(
                                message = response.error?.message ?: FAILED_TO_FETCH_TRANSACTION_ERROR_MESSAGE,
                            )
                        }
                    }
                }
            }
        }

    }

    fun annulTransaction() {
        var transaction = (_transactionDetailScreenData.value as TransactionDetailScreenData.TransactionDetail).transaction
        viewModelScope.launch {
            _transactionDetailScreenData.value = TransactionDetailScreenData.TransactionDetail(
                transaction = transaction,
                transactionAnnulled = TransactionDetailAnnulled.Loading,
            )
            transactionsRepository.postAnnulment(transaction).let { response ->
                when(response) {
                    is Response.Success -> {
                        response.data?.let {
                            transaction = it
                        }
                        _transactionDetailScreenData.value = TransactionDetailScreenData.TransactionDetail(
                            transaction = transaction,
                            transactionAnnulled = TransactionDetailAnnulled.Success(
                                message = "Transacción anulada exitosamente con id de recibo ${transaction.receiptId}",
                            ),
                        )
                    }
                    is Response.Failure -> {
                        _transactionDetailScreenData.value = TransactionDetailScreenData.TransactionDetail(
                            transaction = transaction,
                            transactionAnnulled = TransactionDetailAnnulled.Error(
                                message = response.error?.message ?: "",
                            ),
                        )
                    }
                }
                delay(200)
                _transactionDetailScreenData.value = TransactionDetailScreenData.TransactionDetail(
                    transaction = transaction,
                    transactionAnnulled = TransactionDetailAnnulled.Idle,
                )
            }
        }
    }

    companion object {
        const val TRANSACTION_NOT_FOUND_ERROR_MESSAGE = "Transacción no encontrada"
        const val FAILED_TO_FETCH_TRANSACTION_ERROR_MESSAGE = "Error al obtener la transacción"
    }
}
