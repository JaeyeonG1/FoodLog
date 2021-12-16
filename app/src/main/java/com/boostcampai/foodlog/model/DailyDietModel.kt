package com.boostcampai.foodlog.model

import android.util.Log

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
//    fun sumValueToString(): String{
//        return foods.forEach {  }
//    }
    fun dateToString(): String{
    // date = "YYYY-MM-DD"
        var temp = date.split("-")

        return temp[1] + "월 " + temp[2] + "일"
    }

}
