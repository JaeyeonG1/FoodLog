package com.boostcampai.foodlog.repository

import androidx.lifecycle.LiveData
import com.boostcampai.foodlog.data.dao.DietDao
import com.boostcampai.foodlog.data.dao.DietWithFoods
import com.boostcampai.foodlog.data.dao.FoodDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyRepository @Inject constructor(
    val dietDao: DietDao,
    val foodDao: FoodDao
) {
    fun loadDiet():LiveData<List<DietWithFoods>> =
        foodDao.loadDietWithFoods()
}