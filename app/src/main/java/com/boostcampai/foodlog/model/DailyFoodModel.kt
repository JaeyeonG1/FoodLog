package com.boostcampai.foodlog.model

data class DailyFoodModel(
    val uri: String,
    val time: String,
    val food: Food,
)