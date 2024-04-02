package co.credibanco.transactionstest.transactions.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import co.credibanco.transactionstest.transactions.data.local.model.TransactionEntity

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE status = 'APPROVED'")
    suspend fun getTransactions(): List<TransactionEntity>
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun findTransactionById(id: String): TransactionEntity?
    @Query("SELECT * FROM transactions WHERE receiptId = :receiptId")
    suspend fun findTransactionByReceiptId(receiptId: String): TransactionEntity?
}
