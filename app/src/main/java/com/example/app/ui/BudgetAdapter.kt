package com.example.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ItemBudgetBinding
import com.example.app.model.Budget

class BudgetAdapter(
    private val onEditClick: (Budget) -> Unit,
    private val onDeleteClick: (Budget) -> Unit
) : ListAdapter<Budget, BudgetAdapter.BudgetViewHolder>(BudgetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budget = getItem(position)
        holder.bind(budget)
    }

    inner class BudgetViewHolder(private val binding: ItemBudgetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(budget: Budget) {
            binding.apply {
                tvBudgetName.text = budget.name
                tvBudgetAmount.text = "Số tiền: ${budget.amount}"
                tvBudgetUsed.text = "Đã sử dụng: ${budget.usedAmount}"

                btnEditBudget.setOnClickListener {
                    onEditClick(budget)
                }

                btnDeleteBudget.setOnClickListener {
                    onDeleteClick(budget)
                }
            }
        }
    }

    private class BudgetDiffCallback : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem == newItem
        }
    }
}