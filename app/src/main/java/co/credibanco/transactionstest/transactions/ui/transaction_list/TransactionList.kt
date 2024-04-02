package co.credibanco.transactionstest.transactions.ui.transaction_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import co.credibanco.transactionstest.transactions.model.Transaction
import co.credibanco.transactionstest.ui.utils.hideCardNumber

@Composable
fun TransactionList(
    transactions: List<Transaction>,
    transitionToDetail: (String) -> Unit,
) {
    LazyColumn {
        itemsIndexed(
            transactions,
            key = { _, transaction -> transaction.id }
        ) { _, transaction ->
            TransactionItem(
                modifier = Modifier.padding(bottom = 16.dp),
                transaction = transaction,
                transitionToDetail = transitionToDetail,
            )
        }
    }
}

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    transitionToDetail: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { transitionToDetail(transaction.receiptId) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(0.2f),
        ) {
            Text(
                text = transaction.id,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Column(
            modifier = Modifier.weight(0.8f),
        ) {
            Text("${transaction.commerceCode} - ${transaction.terminalCode}")
            Text("Valor: $${transaction.amount}")
            Text("Tarjeta: ${transaction.card.hideCardNumber()}")
        }

        Icon(
            modifier = Modifier.weight(0.2f),
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Status icon",
        )
    }
}
