package com.example.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app.model.Budget
import com.example.app.model.Category
import com.example.app.model.Transaction

@Database(entities = [Transaction::class, Category::class, Budget::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao // Thêm BudgetDao vào cơ sở dữ liệu

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_tracker_database"
                )
                    .fallbackToDestructiveMigration() // Cho phép xử lý thay đổi schema
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
