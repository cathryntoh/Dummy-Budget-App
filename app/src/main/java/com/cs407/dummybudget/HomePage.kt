package com.cs407.dummybudget

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val graphTitle: TextView = findViewById(R.id.textView3)
        val lineChart: LineChart = findViewById(R.id.lineChart)
        createLineChart(lineChart)
        val pieChart: PieChart = findViewById(R.id.pieChart)
        createPyChart(pieChart, graphTitle)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.action_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_edit_budget -> {
                    startActivity(Intent(this, EditBudget::class.java))
                    true
                }
                R.id.action_living_costs -> {
                    startActivity(Intent(this, LivingCostsScreen::class.java))
                    true
                }
                R.id.action_home -> {
                    startActivity(Intent(this, HomePage::class.java))
                    true
                }
                R.id.action_budget_comparison -> {
                    startActivity(Intent(this, BudgetComparison::class.java))
                    true
                }
                R.id.action_settings -> {
                    startActivity(Intent(this, Settings::class.java))
                    true
                }
                else -> false
            }
        }

    }

    private fun createPyChart(pieChart: PieChart, graphTitle: TextView) {
        // Reference to the LinearLayout
        val lineChartContainer = findViewById<LinearLayout>(R.id.lineChartContainer)

        // Load the background drawable
        val backgroundDrawable = lineChartContainer.background as GradientDrawable
        val pieEntries = listOf(
            PieEntry(15f, "Housing").apply {
                data = mapOf(
                    "category" to "Housing",
                    "color" to "#dfeca0"
                )},
            PieEntry(15f, "Transportation").apply {
                data = mapOf(
                    "category" to "Transportation",
                    "color" to "#cddbaa"
                )},
            PieEntry(15f, "Food").apply {
                data = mapOf(
                    "category" to "Food",
                    "color" to "#9ccaa4"
                )},
            PieEntry(15f, "Utilities").apply {
                data = mapOf(
                    "category" to "Utilities",
                    "color" to "#6db8a6"
                )},
            PieEntry(15f, "Entertainment").apply {
                data = mapOf(
                    "category" to "Entertainment",
                    "color" to "#43a2a9"
                )},
            PieEntry(15f, "Savings").apply {
                data = mapOf(
                    "category" to "Savings",
                    "color" to "#2b8ba8"
                )},
            PieEntry(15f, "Other").apply {
                data = mapOf(
                    "category" to "Other",
                    "color" to "#36729e"
                )},
        )
        val pieDataSet = PieDataSet(pieEntries, "Expenses")
        // Custom Colors (You can add more colors as needed)
        val colors = listOf(
            Color.parseColor("#dfeca0"),
            Color.parseColor("#cddbaa"),
            Color.parseColor("#9ccaa4"),
            Color.parseColor("#6db8a6"),
            Color.parseColor("#43a2a9"),
            Color.parseColor("#2b8ba8"),
            Color.parseColor("#36729e")
        )
        pieDataSet.colors = colors
        pieDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt()}%" // Wrap the value in <b> tags
            }
        }
        val pieData = PieData(pieDataSet)
        pieDataSet.valueTypeface = Typeface.DEFAULT_BOLD
        pieDataSet.valueTextSize = 10f
        pieChart.data = pieData
        pieChart.setDrawEntryLabels(true)
        pieChart.setDrawEntryLabels(false)
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false

        pieChart.invalidate()

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val data = e?.data as? Map<*, *>  // Get the category label
                val category = data?.get("category") as? String ?: ""
                val colorString = data?.get("color") as? String ?: ""
                val color = Color.parseColor(colorString)
                pieChart.centerText = category // Set the category as the center text
                graphTitle.text = "Spending Trends" + " - " + category
                backgroundDrawable.setColor(color)
            }

            override fun onNothingSelected() {
                pieChart.centerText = "" // Reset to default center text
                graphTitle.text = "Spending Trends"
            }
        })
    }

    private fun createLineChart(lineChart: LineChart) {
        // Sample data (replace with your actual data)
        val entries = listOf(
            Entry(0f, 10f),
            Entry(1f, 15f),
            Entry(2f, 8f),
            Entry(3f, 25f),
            Entry(4f, 19f)
        )

        val dataSet = LineDataSet(entries, "Spending Trend")

        // Line chart styling
        dataSet.color = Color.parseColor("#000000") // Set line color
        dataSet.lineWidth = 3f // Set line width
        dataSet.setDrawCircles(false) // Hide data point circles
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER // Smooth curves

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Axis styling
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // X-axis at the bottom
        lineChart.xAxis.axisLineColor = Color.BLACK
        lineChart.axisLeft.axisLineColor = Color.BLACK
        lineChart.xAxis.axisLineWidth = 2f
        lineChart.axisLeft.axisLineWidth = 2f
        lineChart.xAxis.setDrawGridLines(false) // Hide X-axis grid lines
        lineChart.axisLeft.setDrawGridLines(false) // Hide Y-axis grid lines
        lineChart.axisRight.isEnabled = false // Disable right-side Y-axis
        lineChart.xAxis.setDrawLabels(true) // Show X-axis labels
        lineChart.axisLeft.setDrawLabels(true) // Show Y-axis labels
        lineChart.description.isEnabled = false // Hide description
        lineChart.legend.isEnabled = false // Hide legend


        // Background and animation
        lineChart.setDrawGridBackground(false) // Hide grid background
        lineChart.animateX(1000) // Animate chart horizontally

        // X-axis labeling
        lineChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "Week ${value.toInt()}" // Add 1 to start from Week 1
            }
        }

        // Y-axis labeling
        lineChart.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "$" + value.toInt() // Add "$" sign
            }
        }

        lineChart.xAxis.labelCount = 4 // Set the desired number of labels

        lineChart.invalidate()
    }
}