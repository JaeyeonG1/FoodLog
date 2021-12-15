package com.boostcampai.foodlog.model

data class DailyDietModel(
    var date:String,
    var goal:Int,
    var unit:String,
    var foods:List<Food>,
){
    fun foodsToString():String{
        val str = foods.fold("") { sum, now -> "$sum${now.name}, " }
        return str.substring(0, str.length - 2)
    }

}
