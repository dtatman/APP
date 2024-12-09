package com.example.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.DialogAddTransactionBinding
import com.example.app.databinding.FragmentTransactionsBinding
import com.example.app.model.Transaction
import com.example.app.model.TransactionType
import com.example.app.viewmodel.TransactionViewModel
import com.example.app.viewmodel.TransactionViewModelFactory
import com.example.app.repository.TransactionRepository
import com.example.app.database.AppDatabase
import java.text.NumberFormat
import java.util.Locale

class TransactionsFragment : Fragment() {
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!

    // Sử dụng ViewModel với Factory để cung cấp tham số
    private val transactionViewModel: TransactionViewModel by viewModels {
        val transactionDao = AppDatabase.getDatabase(requireContext()).transactionDao()
        val repository = TransactionRepository(transactionDao)
        TransactionViewModelFactory(repository)
    }

    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Thiết lập RecyclerView
        setupRecyclerView()

        // Nút thêm giao dịch
        binding.fabAddTransaction.setOnClickListener {
            showAddTransactionDialog()
        }

        // Observe danh sách giao dịch
        transactionViewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.submitList(transactions)
            updateSummary(transactions)
        }
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter()
        binding.recyclerViewTransactions.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter

            // Thêm swipe to delete
            val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false // Không hỗ trợ kéo
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val transaction = transactionAdapter.currentList[position]
                    transactionViewModel.delete(transaction)
                }
            }

            ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(this)
        }
    }

    private fun showAddTransactionDialog() {
        val dialogBinding = DialogAddTransactionBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Thêm Giao Dịch Mới")
            .setView(dialogBinding.root)
            .setPositiveButton("Thêm") { _, _ ->
                val amount = dialogBinding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
                val description = dialogBinding.etDescription.text.toString()
                val type = if (dialogBinding.rbExpense.isChecked) TransactionType.EXPENSE else TransactionType.INCOME
                if (amount != 0.0) {
                    val transaction = Transaction(
                        amount = amount,
                        description = description,
                        type = type,
                        date = System.currentTimeMillis(),
                        categoryId = 1 // Tạm thời fix cứng
                    )
                    transactionViewModel.insert(transaction)
                }
            }
            .setNegativeButton("Hủy", null)
            .create()
        dialog.show()
    }

    private fun updateSummary(transactions: List<Transaction>) {
        val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        binding.tvTotalIncome.text = "Thu="+formatCurrency(totalIncome)
        binding.tvTotalExpense.text = "Chi="+formatCurrency(totalExpense)
        binding.tvBalance.text = "Hiện kim="+formatCurrency(totalIncome - totalExpense)
    }

    private fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        return format.format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
