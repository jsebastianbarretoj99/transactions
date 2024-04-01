package co.credibanco.transactionstest.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import co.credibanco.transactionstest.datastore.repository.DataStoreRepository
import co.credibanco.transactionstest.transactions.data.TransactionsRepository
import co.credibanco.transactionstest.transactions.model.Response
import co.credibanco.transactionstest.transactions.model.Transaction
import co.credibanco.transactionstest.ui.theme.TransactionsTestTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val dataStoreRepository: DataStoreRepository by inject()
    private val transactionsRepository: TransactionsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TransactionsTestTheme {
                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(key1 = Unit) {
                    dataStoreRepository.writeData(COMMERCE_CODE, "000123").collect { response ->
                        when (response) {
                            is Response.Success -> {
                                Log.d("TEST", response.data ?: "")
                            }
                            is Response.Failure -> {
                                Log.d("TEST", response.error?.message ?: "")
                            }
                        }
                    }
                    dataStoreRepository.writeData(TERMINAL_CODE, "000ABC").collect { response ->
                        when (response) {
                            is Response.Success -> {
                                Log.d("TEST", response.data ?: "")
                            }

                            is Response.Failure -> {
                                Log.d("TEST", response.error?.message ?: "")
                            }
                        }
                    }
                }


                var textRead by remember { mutableStateOf("") }
                var textRemove by remember { mutableStateOf("") }
                var textWrite by remember { mutableStateOf("") }
                var textPostAuthorization by remember { mutableStateOf("") }
                var textPostAnnulment by remember { mutableStateOf("") }
                var textGetReceiptId by remember { mutableStateOf("") }
                var textGetTransactions by remember { mutableStateOf("") }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "Testing Datastore")

                        Button(onClick = {
                            coroutineScope.launch {
                                dataStoreRepository.writeData("Test", "123").collect { response ->
                                    textWrite = when (response) {
                                        is Response.Success -> {
                                            response.data ?: ""
                                        }
                                        is Response.Failure -> {
                                            response.error?.message ?: ""
                                        }
                                    }

                                }
                            }
                        }) {
                            Text(text = "Save Key")
                        }

                        Text(
                            text = "Value Write: $textWrite"
                        )

                        Button(onClick = {
                            coroutineScope.launch {
                                dataStoreRepository.readData("Test").collect { response ->
                                    textRead = when (response) {
                                        is Response.Success -> {
                                            response.data ?: ""
                                        }
                                        is Response.Failure -> {
                                            response.error?.message ?: ""
                                        }
                                    }
                                }
                            }
                        }) {
                            Text(text = "Read Key")
                        }

                        Text(
                            text = "Value Read: $textRead"
                        )

                        Button(onClick = {
                            coroutineScope.launch {
                                dataStoreRepository.removeData("Test").collect {
                                    textRemove = when (it) {
                                        is Response.Success -> {
                                            it.data ?: ""
                                        }
                                        is Response.Failure -> {
                                            it.error?.message ?: ""
                                        }
                                    }
                                }
                            }
                        }) {
                            Text(text = "Remove Key")
                        }

                        Text(
                            text = "Value Remove: $textRemove"
                        )

                        Text(text = "Testing Transactions Repository")

                        Button(onClick = {
                            coroutineScope.launch {
                                val response = transactionsRepository.postAuthorization(
                                    Transaction(
                                        id = "001",
                                        commerceCode = "000123",
                                        terminalCode = "000ABC",
                                        amount = "12345",
                                        card = "1234567890123456"
                                    )
                                )

                                textPostAuthorization = when (response) {
                                    is Response.Success -> {
                                        response.data.toString()
                                    }
                                    is Response.Failure -> {
                                        response.error?.message ?: ""
                                    }
                                }
                            }
                        }) {
                            Text(text = "Call Authorization")
                        }

                        Text(text = "Value Post: $textPostAuthorization")

                        Button(onClick = {
                            coroutineScope.launch {
                                val response = transactionsRepository.postAnnulment(
                                    Transaction(
                                        id = "001",
                                        commerceCode = "000123",
                                        terminalCode = "000ABC",
                                        amount = "12345",
                                        card = "1234567890123456",
                                        receiptId = "e45b016a-d937-44f5-9246-c9839966d09a",
                                        rrn = "ed98d215-97cd-4e3b-91b8-17501a436f0b",
                                    )
                                )

                                textPostAnnulment = when (response) {
                                    is Response.Success -> {
                                        response.data.toString()
                                    }
                                    is Response.Failure -> {
                                        response.error?.message ?: ""
                                    }
                                }
                            }
                        }) {
                            Text(text = "Call Annulment")
                        }

                        Text(text = "Value Annulment: $textPostAnnulment")

                        Button(onClick = {
                            coroutineScope.launch {
                                val response = transactionsRepository.findTransactionByReceiptId(
                                    receiptId = "e45b016a-d937-44f5-9246-c9839966d09a"
                                )
                                textGetReceiptId = when (response) {
                                    is Response.Success -> {
                                        response.data.toString()
                                    }
                                    is Response.Failure -> {
                                        response.error?.message ?: ""
                                    }
                                }
                            }
                        }) {
                            Text(text = "Get ReceiptId")
                        }

                        Text(text = "Value ReceiptId: $textGetReceiptId")

                        Button(onClick = {
                            coroutineScope.launch {
                                val response = transactionsRepository.getTransactions()
                                textGetTransactions = when (response) {
                                    is Response.Success -> {
                                        response.data.toString()
                                    }
                                    is Response.Failure -> {
                                        response.error?.message ?: ""
                                    }
                                }
                            }
                        }) {
                            Text(text = "Get Transactions")
                        }

                        Text(text = "Value Transactions: $textGetTransactions")
                    }
                }
            }
        }
    }

    companion object {
        const val COMMERCE_CODE = "commerceCode"
        const val TERMINAL_CODE = "terminalCode"
    }
}
