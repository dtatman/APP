package com.example.app.repository

import com.example.app.database.TransactionDao
import com.example.app.model.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<Transaction>> =
        transactionDao.getAllTransactions()
    suspend fun insert(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }
    suspend fun update(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }
    suspend fun delete(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }
}