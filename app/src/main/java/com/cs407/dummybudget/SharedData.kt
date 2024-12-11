package com.cs407.dummybudget

object SharedData {
    var budgetCategories: MutableMap<String, Int> = mutableMapOf(
        "Rent" to 1200,
        "Electricity" to 150,
        "Heat" to 300,
        "Water" to 200,
        "Groceries" to 100,
        "Dining Out" to 80,
        "Fuel" to 50,
        "Gym" to 40,
        "Taxes" to 200
    )
}
