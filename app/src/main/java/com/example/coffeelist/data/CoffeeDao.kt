package com.example.coffeelist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CoffeeDao {
    @Query("SELECT * FROM coffee_items")
    fun getAllItems(): Flow<List<CoffeeItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CoffeeItem)

    @Update
    suspend fun updateItem(item: CoffeeItem)

    @Delete
    suspend fun deleteItem(item: CoffeeItem)
}
