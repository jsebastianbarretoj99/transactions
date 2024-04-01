package co.credibanco.transactionstest.database.provider

import android.content.Context
import androidx.room.Room
import co.credibanco.transactionstest.database.AppDatabase

class RoomDatabaseProvider(
    private val appContext: Context,
) {
    fun provideRoomDatabase(): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "transactions-db"
        ).build()
    }
}
