package com.cs407.dummybudget

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settingsLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.action_settings

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
}