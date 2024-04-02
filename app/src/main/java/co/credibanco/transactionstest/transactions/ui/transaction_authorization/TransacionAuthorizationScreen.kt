package co.credibanco.transactionstest.transactions.ui.transaction_authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.credibanco.transactionstest.ui.components.TransactionButton
import co.credibanco.transactionstest.ui.components.TransactionCircularProgressIndicator
import co.credibanco.transactionstest.ui.components.TransactionNumberField
import co.credibanco.transactionstest.ui.utils.showLongToast
import org.koin.androidx.compose.getViewModel

@Composable
fun TransactionAuthorizationScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionAuthorizationViewModel = getViewModel(),
) {

    val screenData by viewModel.transactionAuthorizationScreenData.collectAsState()
    var validateRequired by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        TransactionNumberField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = screenData.transaction.id,
            onValueChange = {
                viewModel.updateTransactionId(it)
            },
            required = true,
            validateRequired = validateRequired,
            label = "ID",
        )

        TransactionNumberField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = screenData.transaction.commerceCode,
            enabled = false,
            label = "Código Comercio",
        )

        TransactionNumberField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = screenData.transaction.terminalCode,
            enabled = false,
            label = "Código Dispositivo",
        )

        TransactionNumberField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = screenData.transaction.amount,
            onValueChange = {
                viewModel.updateAmountTransaction(it)
            },
            required = true,
            validateRequired = validateRequired,
            label = "Monto",
        )

        TransactionNumberField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = screenData.transaction.card,
            onValueChange = {
                viewModel.updateCardTransaction(it)
            },
            required = true,
            validateRequired = validateRequired,
            label = "Tarjeta",
        )

        TransactionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = "Autorizar transacción",
            onClick = {
                validateRequired = true
                viewModel.postTransactionAuthorization()
            }
        )

        when (screenData) {
            is TransactionAuthorizationScreenData.Loading -> {
                TransactionCircularProgressIndicator()
            }

            is TransactionAuthorizationScreenData.TransactionFormData -> {}

            is TransactionAuthorizationScreenData.TransactionAuthorized  -> {
                if ((screenData as TransactionAuthorizationScreenData.TransactionAuthorized).showToast) {
                    viewModel.updateShowToast()
                    context.showLongToast(screenData.message)
                }
            }

            is TransactionAuthorizationScreenData.Error -> {
                if ((screenData as TransactionAuthorizationScreenData.Error).showToast) {
                    viewModel.updateShowToast()
                    context.showLongToast(screenData.message)
                }
            }
        }
    }
}
