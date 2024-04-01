package co.credibanco.transactionstest

import android.app.Application
import co.credibanco.transactionstest.database.koin.ROOM_DATABASE_MODULE
import co.credibanco.transactionstest.datastore.koin.DATA_STORE_MODULE
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
            modules(
                DATA_STORE_MODULE,
                ROOM_DATABASE_MODULE,
                TRANSACTION_MODULE,
            )
        }
    }
}
