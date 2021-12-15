package com.boostcampai.foodlog.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.boostcampai.foodlog.model.Diet
import com.boostcampai.foodlog.model.Food

@Dao
abstract class FoodDao : BaseDao<Food> {
    @Transaction
    @Query("SELECT * FROM Diet WHERE dateTime LIKE '%'||:date||'%' ORDER BY dateTime")
    abstract fun loadDietWithFoodsByDateTime(date: String): LiveData<List<DietWithFoods>>

    @Query("SELECT * FROM Diet ORDER BY dateTime desc")
    abstract fun loadDietWithFoods() : LiveData<List<DietWithFoods>>
}

data class DietWithFoods(
    @Embedded val diet: Diet,
    @Relation(
        parentColumn = "id",
        entityColumn = "imgId"
    )
    val foods: List<Food>
)
