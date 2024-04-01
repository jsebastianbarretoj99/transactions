package co.credibanco.transactionstest.transactions.ui.transaction_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.credibanco.transactionstest.ui.components.TransactionTopBar

@Composable
fun TransactionDetailScreen(
    transitionToHome: () -> Unit,
    receiptId: String?
) {
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
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(text = "Transaction Detail $receiptId")
        }
    }
}
