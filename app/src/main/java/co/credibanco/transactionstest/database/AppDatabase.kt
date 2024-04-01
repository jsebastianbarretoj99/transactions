package co.credibanco.transactionstest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import co.credibanco.transactionstest.transactions.data.local.TransactionDao
import co.credibanco.transactionstest.transactions.data.local.model.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
