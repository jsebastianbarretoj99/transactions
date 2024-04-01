package co.credibanco.transactionstest.database.koin

import co.credibanco.transactionstest.database.provider.RoomDatabaseProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val ROOM_DATABASE_MODULE = module {
    single {
        RoomDatabaseProvider(
            androidContext()
        ).provideRoomDatabase()
    }
}
