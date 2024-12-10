package com.cs407.dummybudget

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val graphTitle: TextView = findViewById(R.id.textView3)
        val lineChart: LineChart = findViewById(R.id.lineChart)
        createLineChart(lineChart)
        val pieChart: PieChart = findViewById(R.id.pieChart)
        createPyChart(pieChart, graphTitle)

    }

    private fun createPyChart(pieChart: PieChart, graphTitle: TextView) {
        val pieEntries = listOf(
            PieEntry(15f, "Housing").apply { data = "Housing" },
            PieEntry(25f, "Transportation").apply { data = "Transportation" },
            PieEntry(10f, "Food").apply { data = "Food" },
            PieEntry(8f, "Utilities").apply { data = "Utilities" },
            PieEntry(12f, "Entertainment").apply { data = "Entertainment" },
            PieEntry(5f, "Savings").apply { data = "Savings" },
            PieEntry(25f, "Other").apply { data = "Other" }
        )
        val pieDataSet = PieDataSet(pieEntries, "Expenses")
        // Custom Colors (You can add more colors as needed)
        val colors = listOf(
            Color.parseColor("#dfeca0"), // Red
            Color.parseColor("#cddbaa"), // Blue
            Color.parseColor("#9ccaa4"), // Yellow
            Color.parseColor("#6db8a6"), // Green
            Color.parseColor("#43a2a9"), // Purple
            Color.parseColor("#2b8ba8"), // Orange
            Color.parseColor("#36729e")  // Gray
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
                val category = e?.data as? String ?: "" // Get the category label
                pieChart.centerText = category // Set the category as the center text
                graphTitle.text = "Spending Trends" + " - " + category
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
        dataSet.color = Color.parseColor("#36729e") // Set line color
        dataSet.lineWidth = 3f // Set line width
        dataSet.setDrawCircles(false) // Hide data point circles
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER // Smooth curves

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Axis styling
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // X-axis at the bottom
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