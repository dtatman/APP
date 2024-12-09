package com.example.app.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.app.databinding.FragmentStatisticsBinding
import com.example.app.model.Transaction
import com.example.app.model.TransactionType
import com.example.app.repository.TransactionRepository
import com.example.app.viewmodel.TransactionViewModel
import com.example.app.viewmodel.TransactionViewModelFactory
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.example.app.database.AppDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    // Khởi tạo TransactionViewModel với Factory
    private val transactionViewModel: TransactionViewModel by viewModels {
        val transactionDao = AppDatabase.getDatabase(requireContext()).transactionDao()
        val repository = TransactionRepository(transactionDao)
        TransactionViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lắng nghe dữ liệu từ ViewModel
        transactionViewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            setupPieChart(transactions)
            setupBarChart(transactions)
        }
    }

    private fun setupPieChart(transactions: List<Transaction>) {
        val pieChart = binding.pieChartExpenses
        val entries = mutableListOf<PieEntry>()

        // Nhóm chi tiêu theo danh mục
        val categoryExpenses = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.categoryId }
            .mapValues { it.value.sumOf { trans -> trans.amount } }

        categoryExpenses.forEach { (categoryId, total) ->
            entries.add(PieEntry(total.toFloat(), "Danh mục $categoryId"))
        }

        val dataSet = PieDataSet(entries, "Chi Tiêu Theo Danh Mục")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Phân Bổ Chi Tiêu"
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun setupBarChart(transactions: List<Transaction>) {
        val barChart = binding.barChartMonthlyTrend
        val entries = mutableListOf<BarEntry>()

        // Nhóm giao dịch theo tháng
        val monthlyTransactions = transactions.groupBy {
            SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(Date(it.date))
        }

        monthlyTransactions.values.forEachIndexed { index, monthTransactions ->
            val totalExpense = monthTransactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            entries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))
        }

        val dataSet = BarDataSet(entries, "Chi Tiêu Hàng Tháng")
        dataSet.color = Color.BLUE

        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
