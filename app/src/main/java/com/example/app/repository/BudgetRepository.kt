package com.example.app.repository

import androidx.annotation.WorkerThread
import com.example.app.model.Budget
import com.example.app.database.BudgetDao
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val budgetDao: BudgetDao) {

    // Lấy tất cả các ngân sách
    val allBudgets: Flow<List<Budget>> = budgetDao.getAllBudgets()

    @WorkerThread
    suspend fun insert(budget: Budget) {
        budgetDao.insertBudget(budget)
    }

    @WorkerThread
    suspend fun update(budget: Budget) {
        budgetDao.updateBudget(budget)
    }

    @WorkerThread
    suspend fun delete(budget: Budget) {
        budgetDao.deleteBudget(budget)
    }
}
