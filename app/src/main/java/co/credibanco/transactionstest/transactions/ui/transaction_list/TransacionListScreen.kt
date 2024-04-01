package co.credibanco.transactionstest.transactions.ui.transaction_list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    transitionToDetail: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Transaction List")

        Button(onClick = { transitionToDetail("12345") }) {
            Text(text = "Go to detail")
        }
    }
}
