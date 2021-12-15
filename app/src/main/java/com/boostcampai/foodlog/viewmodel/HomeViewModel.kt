package com.boostcampai.foodlog.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.boostcampai.foodlog.FoodLogApplication
import java.time.LocalDate

class HomeViewModel : ViewModel() {
    val goal = FoodLogApplication.goalValuePreference
    val unit = FoodLogApplication.goalUnitPreference

    var date = getCurrentDate()
    var current = "170"

    @SuppressLint("NewApi")
    fun getCurrentDate() = LocalDate.now().toString()

    fun getTodayGoalStr(): String {
        return "$current ${unit.value} / ${goal.value} ${unit.value}"
    }

    fun getGoalBadge() = "목표 ${unit.value}"
}
