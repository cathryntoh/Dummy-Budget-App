package com.cs407.dummybudget

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class CityCost(
    val cityName: String,
    val stateAbbr: String,
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

class LivingCostsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_living_costs_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.livingCostsLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // predefined cities and their cost of living
        val cities = listOf(
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

        val cityNames = cities.map { "${it.cityName}, ${it.stateAbbr}" }

        val autoCompleteTextView: AutoCompleteTextView = findViewById(R.id.searchCity)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cityNames)
        autoCompleteTextView.setAdapter(adapter)

        // handle city selection
        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedCityName = parent.getItemAtPosition(position) as String
            val selectedCityNameOnly = selectedCityName.split(",")[0]
            val selectedCity = cities.find { it.cityName == selectedCityNameOnly }
            selectedCity?.let { updateCityDetails(it) }
        }

    }

    // update the placeholders
    private fun updateCityDetails(city: CityCost) {
        findViewById<TextView>(R.id.city2).text = city.cityName + ", " + city.stateAbbr
        findViewById<TextView>(R.id.rentValue2).text = city.rent
        findViewById<TextView>(R.id.electricityValue2).text = city.electricity
        findViewById<TextView>(R.id.heatValue2).text = city.heat
        findViewById<TextView>(R.id.waterValue2).text = city.water
        findViewById<TextView>(R.id.groceriesValue2).text = city.groceries
        findViewById<TextView>(R.id.diningOutValue2).text = city.diningOut
        findViewById<TextView>(R.id.fuelValue2).text = city.fuel
        findViewById<TextView>(R.id.gymValue2).text = city.gym
        findViewById<TextView>(R.id.taxesValue2).text = city.taxes
    }
}
