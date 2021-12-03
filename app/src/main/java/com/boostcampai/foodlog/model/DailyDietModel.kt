package com.boostcampai.foodlog.model

data class DailyDietModel(
    var date:String,
    var total:Int,
    var goal:Int,
    var foods:List<Food>,
)
