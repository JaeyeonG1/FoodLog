package com.boostcampai.foodlog.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    var date = getCurrentDate()
    var current = "170"
    var goal = "1700"
    var unit = "kcal"
//    private var

    @SuppressLint("NewApi")
    fun getCurrentDate() = LocalDate.now().toString()

    fun getTodayGoalStr(): String {
        // DB 에서 current, unit, goal 조회
        return "$current $unit / $goal $unit"
    }
    // DB에서 unit 조회
    fun getBadge() = "목표 $unit"
}
