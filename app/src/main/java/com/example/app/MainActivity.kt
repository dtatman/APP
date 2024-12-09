package com.example.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.app.R
import com.example.app.databinding.ActivityMainBinding
import com.example.app.ui.BudgetFragment
import com.example.app.ui.StatisticsFragment
import com.example.app.ui.TransactionsFragment
import com.example.app.viewmodel.TransactionViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val transactionViewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_transactions -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TransactionsFragment())
                        .commit()
                    true
                }
                R.id.nav_statistics -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, StatisticsFragment())
                        .commit()
                    true
                }
                R.id.nav_budget -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BudgetFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
