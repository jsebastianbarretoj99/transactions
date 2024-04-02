package co.credibanco.transactionstest.transactions.ui.transaction_find

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.credibanco.transactionstest.ui.utils.showLongToast
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFindScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionFindViewModel = getViewModel(),
    transitionToDetail: (String) -> Unit
) {
    val screenData by viewModel.transactionFindScreenData.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getReceiptIdAllTransactions()
    }

    LaunchedEffect(key1 = screenData.transactionFindData.goToDetail) {
        if(screenData.transactionFindData.goToDetail) {
            viewModel.updateGoToDetail()
            transitionToDetail(screenData.transactionFindData.query)
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val transactionFindData = screenData.transactionFindData

        SearchBar(
            query = transactionFindData.query,
            onQueryChange = {
                viewModel.findReceiptId(it)
            },
            onSearch = {
                viewModel.onSearch(it)
            },
            active = transactionFindData.active,
            onActiveChange = {
                viewModel.onActiveChange(it)
            },
            placeholder = {
                Text(text = "Buscar transacción por ID de recibo")
          },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    transactionFindData.receiptIds,
                    key = { receiptId -> receiptId }
                ) {receiptId ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.updateGoToDetail()
                                transitionToDetail(receiptId)
                            }
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Text(text = receiptId)
                    }
                    HorizontalDivider()
                }
            }
        }

        when(screenData) {
            is TransactionFindScreenData.Error -> {
                context.showLongToast((screenData as TransactionFindScreenData.Error).message)
            }

            else -> {}
        }
    }
}
