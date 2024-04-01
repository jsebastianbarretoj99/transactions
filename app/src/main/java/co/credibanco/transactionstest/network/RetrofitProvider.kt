package co.credibanco.transactionstest.network

import co.credibanco.transactionstest.datastore.repository.DataStoreRepository
import co.credibanco.transactionstest.providers.DispatcherProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HeaderInterceptor(dataStoreRepository, dispatcherProvider)
            )
            .build()
    }

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.12:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }
}
