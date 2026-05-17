package com.example.coffeelist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CoffeeItem::class], version = 1, exportSchema = false)
abstract class CoffeeDatabase : RoomDatabase() {
    abstract fun coffeeDao(): CoffeeDao

    companion object {
        @Volatile
        private var INSTANCE: CoffeeDatabase? = null

        fun getDatabase(context: Context): CoffeeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoffeeDatabase::class.java,
                    "coffee_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
