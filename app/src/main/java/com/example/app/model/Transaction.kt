package com.example.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions") // Thêm annotation @Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val categoryId: Long,
    val date: Long,
    val description: String,
    val type: TransactionType
)
