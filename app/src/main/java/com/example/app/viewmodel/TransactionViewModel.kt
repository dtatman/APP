package com.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.app.model.Transaction
import com.example.app.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) :
    ViewModel() {
    val allTransactions: LiveData<List<Transaction>> =
        repository.allTransactions.asLiveData()
    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }
    fun update(transaction: Transaction) = viewModelScope.launch {
        repository.update(transaction)
    }
    fun delete(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }
}

