package com.cs407.dummybudget

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView

class BudgetComparison : AppCompatActivity() {

    data class CityCost(
        val city: String,
        val state: String,
        val rent: String,
        val electricity: String,
        val heat: String,
        val water: String,
        val groceries: String,
        val diningOut: String,
        val fuel: String,
        val gym: String,
        val taxes: String
    )

    private val cities = listOf(
        CityCost("Madison", "WI", "$1,200", "$150", "$300", "$200", "$100", "$80", "$50", "$40", "$200"),
        CityCost("Austin", "TX", "$1,500", "$180", "$320", "$250", "$120", "$100", "$60", "$50", "$250"),
        CityCost("Denver", "CO", "$1,400", "$160", "$310", "$220", "$110", "$90", "$55", "$45", "$240"),
        CityCost("San Francisco", "CA", "$3,000", "$250", "$400", "$300", "$200", "$150", "$80", "$70", "$400"),
        CityCost("New York", "NY", "$3,500", "$300", "$450", "$350", "$250", "$200", "$100", "$90", "$500"),
        CityCost("Chicago", "IL", "$1,800", "$200", "$350", "$270", "$140", "$120", "$70", "$60", "$300"),
        CityCost("Los Angeles", "CA", "$2,700", "$220", "$380", "$280", "$180", "$140", "$75", "$65", "$350"),
        CityCost("Seattle", "WA", "$2,100", "$200", "$360", "$260", "$160", "$120", "$70", "$60", "$300"),
        CityCost("Miami", "FL", "$2,000", "$180", "$340", "$230", "$140", "$110", "$65", "$55", "$250"),
        CityCost("Boston", "MA", "$2,500", "$230", "$400", "$300", "$190", "$140", "$75", "$65", "$350"),
        CityCost("Dallas", "TX", "$1,600", "$170", "$330", "$240", "$130", "$110", "$60", "$50", "$240"),
        CityCost("Houston", "TX", "$1,550", "$160", "$320", "$230", "$120", "$100", "$60", "$50", "$230"),
        CityCost("Philadelphia", "PA", "$1,700", "$190", "$340", "$250", "$130", "$110", "$65", "$55", "$280"),
        CityCost("Atlanta", "GA", "$1,800", "$190", "$350", "$260", "$140", "$120", "$70", "$60", "$300"),
        CityCost("Phoenix", "AZ", "$1,400", "$160", "$320", "$230", "$120", "$100", "$60", "$50", "$240")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.action_budget_comparison // Update for each activity

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


        val cityFilterSpinner = findViewById<Spinner>(R.id.cityFilterSpinner)
        val budgetComparisonChart = findViewById<BarChart>(R.id.budgetComparisonChart)
        val budgetComparisonContainer = findViewById<LinearLayout>(R.id.budgetComparisonContainer)
        val selectedCityName = findViewById<TextView>(R.id.selectedCityName)

        // Populate spinner
        val cityNames = cities.map { "${it.city}, ${it.state}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cityNames)
        cityFilterSpinner.adapter = adapter

        // Set default selection
        cityFilterSpinner.setSelection(1)

        fun populateComparisonData(selectedCity: CityCost) {
            // Clear previous rows
            budgetComparisonContainer.removeAllViews()

            // Update selected city name
            selectedCityName.text =
                getString(R.string.city_state_format, selectedCity.city, selectedCity.state)

            val madison = SharedData.budgetCategories

            val categories = listOf(
                "Rent" to Pair(
                    madison["Rent"]?.toFloat() ?: 1200f,
                    selectedCity.rent.replace("$", "").replace(",", "").toFloat()
                ),
                "Electricity" to Pair(
                    madison["Electricity"]?.toFloat() ?: 150f,
                    selectedCity.electricity.replace("$", "").replace(",", "").toFloat()
                ),
                "Heat" to Pair(
                    madison["Heat"]?.toFloat() ?: 300f,
                    selectedCity.heat.replace("$", "").replace(",", "").toFloat()
                ),
                "Water" to Pair(
                    madison["Water"]?.toFloat() ?: 200f,
                    selectedCity.water.replace("$", "").replace(",", "").toFloat()
                ),
                "Groceries" to Pair(
                    madison["Groceries"]?.toFloat() ?: 100f,
                    selectedCity.groceries.replace("$", "").replace(",", "").toFloat()
                ),
                "Dining Out" to Pair(
                    madison["Dining Out"]?.toFloat() ?: 80f,
                    selectedCity.diningOut.replace("$", "").replace(",", "").toFloat()
                ),
                "Fuel" to Pair(
                    madison["Fuel"]?.toFloat() ?: 50f,
                    selectedCity.fuel.replace("$", "").replace(",", "").toFloat()
                ),
                "Gym" to Pair(
                    madison["Gym"]?.toFloat() ?: 40f,
                    selectedCity.gym.replace("$", "").replace(",", "").toFloat()
                ),
                "Taxes" to Pair(
                    madison["Taxes"]?.toFloat() ?: 200f,
                    selectedCity.taxes.replace("$", "").replace(",", "").toFloat()
                )
            )

            // Define button colors for categories
            val buttonColors = listOf(
                R.color.purple, R.color.teal, R.color.green, R.color.red, R.color.cyan,
                R.color.orange, R.color.pink, R.color.blue, R.color.yellow
            )

            // Create rows dynamically
            for ((index, entry) in categories.withIndex()) {
                val (category, values) = entry

                val row = LinearLayout(this@BudgetComparison).apply {
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(16, 16, 16, 16)
                }

                val madisonValue = TextView(this@BudgetComparison).apply {
                    text = "$${values.first.toInt()}"
                    layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    textSize = 16f
                }

                val categoryButton = Button(this@BudgetComparison).apply {
                    text = category
                    layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@BudgetComparison,
                            buttonColors[index % buttonColors.size]
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this@BudgetComparison,
                            android.R.color.white
                        )
                    )
                }

                val selectedCityValue = TextView(this@BudgetComparison).apply {
                    text = "$${values.second.toInt()}"
                    layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    textSize = 16f
                }

                row.addView(madisonValue)
                row.addView(categoryButton)
                row.addView(selectedCityValue)
                budgetComparisonContainer.addView(row)
            }

            // Populate Bar Chart
            val entriesCity1 = mutableListOf<BarEntry>()
            val entriesCity2 = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()

            for ((index, entry) in categories.withIndex()) {
                labels.add(entry.first) // Add category name to labels
                entriesCity1.add(BarEntry(index.toFloat(), entry.second.first)) // Madison data
                entriesCity2.add(BarEntry(index.toFloat(), entry.second.second)) // Selected city data
            }

            val dataSetCity1 = BarDataSet(entriesCity1, "Madison, WI").apply {
                color = ContextCompat.getColor(this@BudgetComparison, R.color.teal)
                valueTextSize = 12f
            }

            val dataSetCity2 = BarDataSet(entriesCity2, selectedCity.city).apply {
                color = ContextCompat.getColor(this@BudgetComparison, R.color.orange)
                valueTextSize = 12f
            }

            val barData = BarData(dataSetCity1, dataSetCity2).apply {
                barWidth = 0.3f // Adjust bar width
            }

            budgetComparisonChart.apply {
                data = barData
                description.isEnabled = false
                setPinchZoom(false)
                setScaleEnabled(false)
                animateY(1000)

                // Adjust x-axis
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    valueFormatter = IndexAxisValueFormatter(labels) // Center labels
                    granularity = 1f // Ensure even spacing
                    setDrawGridLines(false)
                    textSize = 12f
                    axisMinimum = -0.5f // Start slightly before the first bar group
                    axisMaximum = labels.size.toFloat() - 0.5f // End slightly after the last bar group
                    labelRotationAngle = 45f // Tilt labels at a 45-degree angle
                }

                // Configure left axis
                axisLeft.apply {
                    axisMinimum = 0f // Ensure the y-axis starts at 0
                    textSize = 12f
                }

                // Disable right axis
                axisRight.isEnabled = false

                // Group bars with proper spacing
                groupBars(-0.5f, 0.3f, 0.05f) // Ensure proper spacing and label alignment

                // Horizontal scrolling for large charts
                setVisibleXRangeMaximum(6f) // Show a maximum of 6 categories at a time
                moveViewToX(0f) // Start viewing from the first category

                invalidate() // Refresh the chart
            }
        }

            // Default populate
        populateComparisonData(cities[1])

        // Update on spinner selection
        cityFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                populateComparisonData(cities[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}



