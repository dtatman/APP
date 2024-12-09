package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.app.model.Budget
import com.example.app.repository.BudgetRepository
import kotlinx.coroutines.launch

class BudgetViewModel(private val repository: BudgetRepository) : ViewModel() {

    // Quan sát tất cả ngân sách dưới dạng LiveData
    val allBudgets = repository.allBudgets.asLiveData()

    fun insert(budget: Budget) = viewModelScope.launch {
        repository.insert(budget)
    }

    fun update(budget: Budget) = viewModelScope.launch {
        repository.update(budget)
    }

    fun delete(budget: Budget) = viewModelScope.launch {
        repository.delete(budget)
    }
}
