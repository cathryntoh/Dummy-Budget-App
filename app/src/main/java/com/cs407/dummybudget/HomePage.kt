package com.cs407.dummybudget

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
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
        val lineChart: LineChart = findViewById(R.id.lineChart)

        // Creating a list of Entry objects (x, y values)
        val entries = listOf(
            Entry(0f, 1f),
            Entry(1f, 2f),
            Entry(2f, 3f),
            Entry(3f, 5f)
        )

        // Create a dataset and give it a type (LineDataSet)
        val dataSet = LineDataSet(entries, "Label")

        // Create a LineData object with the dataset
        val lineData = LineData(dataSet)

        // Set the data for the chart
        lineChart.data = lineData

        // Refresh the chart to show data
        lineChart.invalidate()

        // Pie Chart Code (New)
        val pieChart: PieChart = findViewById(R.id.pieChart)
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
            }

            override fun onNothingSelected() {
                pieChart.centerText = "" // Reset to default center text
            }
        })


    }

}