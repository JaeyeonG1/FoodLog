package com.boostcampai.foodlog.model

data class Food(
    var id:Long,
    var imgId:Long,
    var cls:Int,
    var name:String,
    var pos:Position,
    var nutrition:Nutrition
)
