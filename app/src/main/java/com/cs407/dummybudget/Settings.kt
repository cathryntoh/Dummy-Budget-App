package com.cs407.dummybudget

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Settings : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()

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

        findViewById<LinearLayout>(R.id.accountButton).setOnClickListener {
            showChangePasswordDialog()
        }

        findViewById<LinearLayout>(R.id.notificationsButton).setOnClickListener {
            Toast.makeText(this, "Notifications settings clicked", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.privacyButton).setOnClickListener {
            deleteAccount()
        }

        findViewById<LinearLayout>(R.id.helpButton).setOnClickListener {
            Toast.makeText(this, "Help & Support clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showChangePasswordDialog() {
        if (auth.currentUser == null) {
            Toast.makeText(this, "No logged-in user", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a dialog with an input field for the new password
        val dialogBuilder = AlertDialog.Builder(this)
        val input = EditText(this).apply {
            hint = "Enter new password"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        dialogBuilder.setTitle("Change Password")
            .setView(input)
            .setPositiveButton("Submit") { _, _ ->
                val newPassword = input.text.toString()
                if (newPassword.isNotEmpty() && newPassword.length >= 6) {
                    // Update the password in Firebase
                    auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error updating password: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun deleteAccount() {
        if (auth.currentUser == null) {
            Toast.makeText(this, "No logged-in user", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Yes") { _, _ ->
                auth.currentUser?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                        auth.signOut()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error deleting account: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
