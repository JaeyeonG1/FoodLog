package com.boostcampai.foodlog.data.dao

import androidx.room.Dao
import com.boostcampai.foodlog.model.Food

@Dao
abstract class FoodDao: BaseDao<Food> {
}
