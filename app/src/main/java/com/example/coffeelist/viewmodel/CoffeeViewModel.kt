package com.example.coffeelist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeelist.data.CoffeeDatabase
import com.example.coffeelist.data.CoffeeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CoffeeViewModel(application: Application) : AndroidViewModel(application) {
    private val coffeeDao = CoffeeDatabase.getDatabase(application).coffeeDao()
    val allItems: Flow<List<CoffeeItem>> = coffeeDao.getAllItems()

    fun addItem(name: String, type: String, price: Double) {
        viewModelScope.launch {
            coffeeDao.insertItem(CoffeeItem(name = name, type = type, price = price))
        }
    }

    fun updateItem(item: CoffeeItem) {
        viewModelScope.launch {
            coffeeDao.updateItem(item)
        }
    }

    fun deleteItem(item: CoffeeItem) {
        viewModelScope.launch {
            coffeeDao.deleteItem(item)
        }
    }

    fun addItemToCart(item: CoffeeItem) {
        viewModelScope.launch {
            coffeeDao.updateItem(item.copy(isInCart = true, quantity = item.quantity + 1))
        }
    }

    fun updateQuantity(item: CoffeeItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                coffeeDao.updateItem(item.copy(isInCart = false, quantity = 0))
            } else {
                coffeeDao.updateItem(item.copy(quantity = newQuantity))
            }
        }
    }
}
