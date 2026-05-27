package com.example.coffeelist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coffee_items")
data class CoffeeItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String,
    val price: Double,
    val isInCart: Boolean = false,
    val quantity: Int = 0
)
