package com.boostcampai.foodlog.model

data class InferenceResult(
    val date: String,
    val time: String,
    val status: String,
    var foods: List<Food>
)
