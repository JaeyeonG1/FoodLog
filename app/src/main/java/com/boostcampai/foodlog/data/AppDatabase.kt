package com.boostcampai.foodlog.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.boostcampai.foodlog.data.dao.DietDao
import com.boostcampai.foodlog.data.dao.FoodDao
import com.boostcampai.foodlog.model.Diet
import com.boostcampai.foodlog.model.Food

@Database(entities = [Diet::class, Food::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dietDao(): DietDao
    abstract fun foodDao(): FoodDao
}
