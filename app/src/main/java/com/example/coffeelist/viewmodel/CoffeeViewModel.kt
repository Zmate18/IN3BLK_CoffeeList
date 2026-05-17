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
}
