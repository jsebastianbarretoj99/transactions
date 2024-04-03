package co.credibanco.transactionstest

import android.app.Application
import android.util.Log
import co.credibanco.transactionstest.database.koin.ROOM_DATABASE_MODULE
import co.credibanco.transactionstest.datastore.koin.DATA_STORE_MODULE
import co.credibanco.transactionstest.datastore.repository.DataStoreRepository
import co.credibanco.transactionstest.providers.DispatcherProvider
import co.credibanco.transactionstest.transactions.koin.TRANSACTION_MODULE
import co.credibanco.transactionstest.transactions.model.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(androidContext = this@BaseApplication)
            if (BuildConfig.DEBUG) {
                androidLogger(level = Level.DEBUG)
            }
            modules(
                DATA_STORE_MODULE,
                ROOM_DATABASE_MODULE,
                TRANSACTION_MODULE,
            )
        }

        val dispatcherProvider: DispatcherProvider by inject()
        val dataStoreRepository: DataStoreRepository by inject()

        val coroutineScope = CoroutineScope(dispatcherProvider.getMain())

        coroutineScope.launch {
            dataStoreRepository.writeData(COMMERCE_CODE, "000123").collect {
                when (it) {
                    is Response.Success -> {
                        Log.e("BaseApplication", "$COMMERCE_CODE Data stored")
                    }
                    is Response.Failure -> {
                        Log.e("BaseApplication", "$COMMERCE_CODE Data not stored")
                    }
                }
            }
        }

        coroutineScope.launch {
            dataStoreRepository.writeData(TERMINAL_CODE, "000ABC").collect {
                when (it) {
                    is Response.Success -> {
                        Log.e("BaseApplication", "$TERMINAL_CODE Data stored")
                    }
                    is Response.Failure -> {
                        Log.e("BaseApplication", "$TERMINAL_CODE Data not stored")
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
