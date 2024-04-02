package co.credibanco.transactionstest.transactions.ui.transaction_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.credibanco.transactionstest.ui.components.TransactionCircularProgressIndicator
import co.credibanco.transactionstest.ui.utils.showLongToast
import org.koin.androidx.compose.getViewModel

@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    transitionToDetail: (String) -> Unit,
    viewModel: TransactionListViewModel = getViewModel(),
) {
    val context = LocalContext.current
    val screenData by viewModel.transactionListScreenData.collectAsState()

    val verticalArrangement = if (screenData is TransactionListScreenData.Transactions)
        Arrangement.Top else Arrangement.Center

    Column(
        modifier = modifier.padding(top = 16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = verticalArrangement,
    ) {
        when(screenData) {
            is TransactionListScreenData.Loading -> {
                TransactionCircularProgressIndicator()
            }

            is TransactionListScreenData.Transactions -> {
                TransactionList(
                    transactions = (screenData as TransactionListScreenData.Transactions).transactions,
                    transitionToDetail = transitionToDetail,
                )
            }

            is TransactionListScreenData.Error -> {
                context.showLongToast(screenData.message)
            }

            is TransactionListScreenData.Empty -> {
                Text(text = screenData.message)
            }
        }
    }
}
