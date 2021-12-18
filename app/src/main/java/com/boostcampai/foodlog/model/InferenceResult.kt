package com.boostcampai.foodlog.model

data class InferenceResult(
    val date: String,
    val time: String,
    val status: String,
    var foods: List<Food>
) {
    fun getPresentDate(): String {
        var temp = date.split("-")
        return temp[1] + "월 " + temp[2] + "일"
    }

    fun getPresentTime(): String {
        var temp = time.split(":")
        return temp[0] + ":" + temp[1]
    }
}
