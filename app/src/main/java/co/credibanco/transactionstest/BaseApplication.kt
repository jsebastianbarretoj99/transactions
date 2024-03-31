package co.credibanco.transactionstest

import android.app.Application
import co.credibanco.transactionstest.transactions.koin.TRANSACTION_MODULE
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
            modules(TRANSACTION_MODULE)
        }
    }
}