package co.credibanco.transactionstest.transactions.ui.transaction_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.credibanco.transactionstest.transactions.model.TransactionStatus
import co.credibanco.transactionstest.ui.components.TransactionCircularProgressIndicator
import co.credibanco.transactionstest.ui.components.TransactionTopBar
import co.credibanco.transactionstest.ui.utils.hideCardNumber
import co.credibanco.transactionstest.ui.utils.showLongToast
import org.koin.androidx.compose.getViewModel

@Composable
fun TransactionDetailScreen(
    transitionToHome: () -> Unit,
    receiptId: String?,
    viewModel: TransactionDetailViewModel = getViewModel(),
) {

    val screenData by viewModel.transactionDetailScreenData.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getTransactionDetail(receiptId = receiptId)
    }

    Scaffold(
        topBar = {
            TransactionTopBar(
             title = "Detalle de Transacción",
             navigationIcon = {
                 Box(
                     modifier = Modifier.clickable(
                         onClick = { transitionToHome() }
                     )
                 ) {
                     Icon(
                         imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                         contentDescription = "Back",
                         modifier = Modifier.padding(24.dp)
                     )
                 }
             }
         )
        }
    ) { innerPadding ->
        when(screenData) {
            is TransactionDetailScreenData.TransactionDetail -> {
                val transactionDetail = (screenData as TransactionDetailScreenData.TransactionDetail)
                Column(
                    modifier = Modifier.padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val transaction = transactionDetail.transaction

                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Id: ${transaction.id}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Código de Comercio: ${transaction.commerceCode}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Código de Terminal: ${transaction.terminalCode}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Valor: $${transaction.amount}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Tarjeta: ${transaction.card.hideCardNumber()}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Id de Recibo: ${transaction.receiptId}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "RRN: ${transaction.rrn}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(text = "Estado: ")
                            Icon(
                                imageVector = if (transaction.status == TransactionStatus.APPROVED) {
                                    Icons.Default.CheckCircle
                                } else {
                                    Icons.Default.Clear
                                },
                                contentDescription = "Estado",
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.annulTransaction()
                            }
                        ) {
                            Text("Anular transacción")
                        }
                    }

                    val transactionAnnulled = transactionDetail.transactionAnnulled

                    if (transactionAnnulled.showToast) {
                        context.showLongToast(transactionAnnulled.message)
                    }

                    when(transactionAnnulled) {
                        is TransactionDetailAnnulled.Loading -> {
                            TransactionCircularProgressIndicator()
                        }
                        else -> {}
                    }
                }
            }
            is TransactionDetailScreenData.Error -> {
                Column(
                    modifier = Modifier.padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val message = (screenData as TransactionDetailScreenData.Error).message
                    Text(
                        "Error: $message",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            is TransactionDetailScreenData.Loading -> {
                Column(
                    modifier = Modifier.padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TransactionCircularProgressIndicator()
                }
            }
        }
    }
}
