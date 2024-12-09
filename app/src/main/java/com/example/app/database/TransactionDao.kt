package com.example.app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.app.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>
    @Insert
    suspend fun insertTransaction(transaction: Transaction)
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
}