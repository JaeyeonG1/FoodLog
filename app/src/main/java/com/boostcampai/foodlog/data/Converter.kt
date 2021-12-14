package com.boostcampai.foodlog.data

import androidx.room.TypeConverter
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.model.Position
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun foodToJson(food: Food): String = Gson().toJson(food)

    @TypeConverter
    fun jsonToFood(str: String): Food = Gson().fromJson(str, Food::class.java)

    @TypeConverter
    fun positionToJson(pos: Position): String = Gson().toJson(pos)

    @TypeConverter
    fun jsonToPosition(str: String): Position = Gson().fromJson(str, Position::class.java)
}
