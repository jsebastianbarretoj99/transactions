package co.credibanco.transactionstest.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TransactionNumberField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    required: Boolean = false,
    validateRequired: Boolean = false,
    label: String,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            ),
            label = { Text(text = label) },
            enabled = enabled,
            singleLine = true,
        )
        if (required && validateRequired && value.isEmpty()) {
            Text(
                text = "$label es obligatorio",
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
