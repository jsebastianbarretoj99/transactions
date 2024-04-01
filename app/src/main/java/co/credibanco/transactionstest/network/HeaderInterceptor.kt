package co.credibanco.transactionstest.network

import co.credibanco.transactionstest.datastore.repository.DataStoreRepository
import co.credibanco.transactionstest.providers.DispatcherProvider
import co.credibanco.transactionstest.transactions.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import java.util.Base64
import okhttp3.Response as OkHttpResponse

class HeaderInterceptor(
    private val dataStoreRepository: DataStoreRepository,
    dispatcherProvider: DispatcherProvider,
): Interceptor {

    private val coroutineScope = CoroutineScope(dispatcherProvider.getIO())

    override fun intercept(chain: Interceptor.Chain): OkHttpResponse {
        val commerceCodeDeferred = coroutineScope.async {
            dataStoreRepository.readData(COMMERCE_CODE)
        }
        val terminalCodeDeferred = coroutineScope.async {
            dataStoreRepository.readData(TERMINAL_CODE)
        }

        val commerceCodeResponse = runBlocking { commerceCodeDeferred.await().first() }
        val terminalCodeResponse = runBlocking { terminalCodeDeferred.await().first() }

        val commerceCode = when (commerceCodeResponse) {
            is Response.Success -> commerceCodeResponse.data ?: ""
            else -> ""
        }

        val terminalCode = when (terminalCodeResponse) {
            is Response.Success -> terminalCodeResponse.data ?: ""
            else -> ""
        }

        val base64 = Base64.getEncoder().encodeToString((commerceCode + terminalCode).toByteArray())

        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header(AUTHORIZATION, "Basic $base64")
            .build()
        return chain.proceed(request)
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val COMMERCE_CODE = "commerceCode"
        const val TERMINAL_CODE = "terminalCode"
    }
}
