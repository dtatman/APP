package com.example.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.databinding.DialogAddBudgetBinding
import com.example.app.databinding.FragmentBudgetBinding
import com.example.app.model.Budget
import com.example.app.viewmodel.BudgetViewModel
import com.example.app.viewmodel.BudgetViewModelFactory
    import com.example.app.repository.BudgetRepository
import com.example.app.database.AppDatabase
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class BudgetFragment : Fragment() {
    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!

    private val budgetViewModel: BudgetViewModel by viewModels {
        val budgetDao = AppDatabase.getDatabase(requireContext()).budgetDao()
        val repository = BudgetRepository(budgetDao)
        BudgetViewModelFactory(repository)
    }

    private lateinit var budgetAdapter: BudgetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Thiết lập RecyclerView
        setupRecyclerView()

        // Nút thêm ngân sách
        binding.fabAddBudget.setOnClickListener {
            showAddBudgetDialog()
        }

        // Quan sát danh sách ngân sách
        budgetViewModel.allBudgets.observe(viewLifecycleOwner) { budgets ->
            budgetAdapter.submitList(budgets)
            updatePieChart(budgets)
        }
    }

    private fun setupRecyclerView() {
        budgetAdapter = BudgetAdapter(
            onEditClick = { budget ->
                // Thực hiện hành động chỉnh sửa ngân sách
            },
            onDeleteClick = { budget ->
                // Thực hiện hành động xóa ngân sách
                budgetViewModel.delete(budget)
            }
        )
        binding.recyclerViewBudgets.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = budgetAdapter
        }
    }

    private fun showAddBudgetDialog() {
        val dialogBinding = DialogAddBudgetBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Thêm Ngân Sách")
            .setView(dialogBinding.root)
            .setPositiveButton("Thêm") { _, _ ->
                val name = dialogBinding.etBudgetName.text.toString()
                val amount = dialogBinding.etBudgetAmount.text.toString().toDoubleOrNull() ?: 0.0
                if (name.isNotBlank() && amount > 0) {
                    val budget = Budget(
                        name = name,
                        amount = amount,
                        usedAmount = 0.0 // Mới tạo chưa có chi tiêu
                    )
                    budgetViewModel.insert(budget)
                }
            }
            .setNegativeButton("Hủy", null)
            .create()
        dialog.show()
    }


    private fun updatePieChart(budgets: List<Budget>) {
        // Kiểm tra nếu không có dữ liệu
        if (budgets.isEmpty()) {
            binding.pieChartBudget.visibility = View.GONE
            return
        } else {
            binding.pieChartBudget.visibility = View.VISIBLE
        }

        val pieEntries = budgets.map {
            // Chắc chắn rằng usedAmount > 0, nếu không sẽ không vẽ được
            PieEntry(it.usedAmount.toFloat(), it.name)
        }

        if (pieEntries.isEmpty()) {
            return
        }

        val pieDataSet = PieDataSet(pieEntries, "Ngân Sách")
        pieDataSet.colors = listOf(
            resources.getColor(R.color.blue, null),
            resources.getColor(R.color.expense_red, null),
            resources.getColor(R.color.income_green, null)
        )

        val pieData = PieData(pieDataSet)
        binding.pieChartBudget.apply {
            data = pieData
            invalidate() // Làm mới biểu đồ
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
