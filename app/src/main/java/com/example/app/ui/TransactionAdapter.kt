package com.example.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.databinding.ItemTransactionBinding
import com.example.app.model.Transaction
import com.example.app.model.TransactionType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter : ListAdapter<Transaction,
        TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }
    override fun onBindViewHolder(holder: TransactionViewHolder, position:
    Int) {
        holder.bind(getItem(position))
    }
    inner class TransactionViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.apply {
                tvAmount.text = formatCurrency(transaction.amount)
                tvDescription.text = transaction.description
                tvDate.text = formatDate(transaction.date)
// Đặt màu và icon dựa trên loại giao dịch
                if (transaction.type == TransactionType.EXPENSE) {
                    tvAmount.setTextColor(
                        ContextCompat.getColor(root.context,
                        R.color.expense_red))
                    tvAmount.text = "- ${formatCurrency(transaction.amount)}"
                } else {
                    tvAmount.setTextColor(
                        ContextCompat.getColor(root.context,
                        R.color.income_green))
                    tvAmount.text = "+ ${formatCurrency(transaction.amount)}"
                }
            }
        }
    }
    // Callback so sánh các transaction
    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem:
        Transaction): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Transaction, newItem:
        Transaction): Boolean {
            return oldItem == newItem
        }
    }
    // Utility functions
    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(
            Locale("vi",
            "VN")
        ).format(amount)
    }
    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}