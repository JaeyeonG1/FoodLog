package com.boostcampai.foodlog.model

data class DailyDietModel(
    var date:String,
    var goal:Int,
    var foods:List<Food>,
){
    fun foodsToString():String{
        val str = foods.fold("") { sum, now -> "$sum${now.name}, " }
        return str.substring(0, str.length - 2)
    }

    fun getNutritionSumString():String{
        if (foods.isEmpty())
            return "0"

        val sum = foods.map { it.nutrition.value }.fold(0f) { sum, now -> sum + now }
        return sum.toInt().toString() + " " + foods[0].nutrition.unit + " " + "/" + goal.toString() + foods[0].nutrition.unit
    }
}
