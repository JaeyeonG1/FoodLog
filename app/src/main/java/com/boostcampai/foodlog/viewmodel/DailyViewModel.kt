package com.boostcampai.foodlog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.boostcampai.foodlog.FoodLogApplication
import com.boostcampai.foodlog.data.dao.DietWithFoods
import com.boostcampai.foodlog.model.DailyFoodModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
) : ViewModel() {
    lateinit var date: String
    val unit: LiveData<String> = FoodLogApplication.goalUnitPreference
    var dailyFoods: MutableList<DailyFoodModel> = mutableListOf()

    fun initDailyFoods(dietWithFoods: List<DietWithFoods>) {
        date = getDate(dietWithFoods[0].diet.dateTime)

        dietWithFoods.forEach {
            dailyFoods.addAll(it.foods.map { food ->
                Log.d("food item", food.toString())
                DailyFoodModel(it.diet.uri, getTime(it.diet.dateTime), food)
            })
        }
        Log.d("initDailyFoods", dailyFoods.size.toString())
    }

    private fun getDate(date: String): String {
        // String form is 'YYYY-MM-DD HH:MM:SS'
        var temp = date.split(" ")[0].split("-")

        return temp[1] + "월 " + temp[2] + "일"
    }

    private fun getTime(date: String): String {
        // String form is 'YYYY-MM-DD HH:MM:SS'
        var temp = date.split(" ")[1].split(":")

        return temp[0] + ":" + temp[1]
    }
}
