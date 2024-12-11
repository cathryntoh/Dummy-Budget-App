package com.cs407.dummybudget

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.widget.EditText
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditBudget : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edityourbudget)

        // Bottom navigation setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.action_edit_budget

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

        val budgetLayout = findViewById<LinearLayout>(R.id.budgetLayout)

        // Dynamically populate the budget categories from SharedData
        budgetLayout.removeAllViews() // Clear any previous views
        populateBudget(budgetLayout)
    }

    private fun populateBudget(budgetLayout: LinearLayout) {
        // Sort categories by amount in descending order
        val sortedCategories = SharedData.budgetCategories.toList().sortedByDescending { it.second }

        // Find the largest value to normalize bar lengths
        val maxAmount = sortedCategories.maxOfOrNull { it.second } ?: 1 // Avoid division by zero

        // Add categories dynamically
        for ((category, amount) in sortedCategories) {
            val categoryView = LayoutInflater.from(this)
                .inflate(R.layout.item_budget_row, budgetLayout, false)

            val categoryName = categoryView.findViewById<TextView>(R.id.categoryName)
            val categoryAmount = categoryView.findViewById<TextView>(R.id.categoryAmount)
            val categoryBar = categoryView.findViewById<View>(R.id.categoryBar)
            val inputBox = categoryView.findViewById<EditText>(R.id.editAmount)
            val submitButton = categoryView.findViewById<MaterialButton>(R.id.submitButton)

            // Set initial values
            categoryName.text = category
            categoryAmount.text = "$$amount"

            // Scale the bar length based on the max amount
            val normalizedWidth = (amount.toFloat() / maxAmount * 300).toInt() // Scale max width to 300dp
            val layoutParams = categoryBar.layoutParams
            layoutParams.width = normalizedWidth
            categoryBar.layoutParams = layoutParams

            // Align category amount uniformly after the longest bar
            val amountMarginParams = categoryAmount.layoutParams as LinearLayout.LayoutParams
            amountMarginParams.marginStart = 300 - normalizedWidth
            categoryAmount.layoutParams = amountMarginParams

            // Submit button logic
            submitButton.setOnClickListener {
                val newAmount = inputBox.text.toString().toIntOrNull()
                if (newAmount != null) {
                    // Update SharedData with the new value
                    SharedData.budgetCategories[category] = newAmount

                    // Update UI
                    categoryAmount.text = "$$newAmount"

                    // Recalculate bar length
                    val updatedWidth = (newAmount.toFloat() / maxAmount * 300).toInt()
                    categoryBar.layoutParams.width = updatedWidth

                    // Update alignment
                    amountMarginParams.marginStart = 300 - updatedWidth
                    categoryAmount.layoutParams = amountMarginParams

                    inputBox.text.clear()
                    Toast.makeText(this, "$category updated to $$newAmount", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            }

            budgetLayout.addView(categoryView)
        }
    }
}