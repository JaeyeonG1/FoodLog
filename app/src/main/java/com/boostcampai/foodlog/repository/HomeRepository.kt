package com.boostcampai.foodlog.repository

import androidx.lifecycle.LiveData
import com.boostcampai.foodlog.data.dao.DietDao
import com.boostcampai.foodlog.data.dao.DietWithFoods
import com.boostcampai.foodlog.data.dao.FoodDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    val dietDao: DietDao,
    val foodDao: FoodDao
) {
    fun loadDiet(date: String): LiveData<List<DietWithFoods>> =
        foodDao.loadDietWithFoodsByDateTime(date)
}
