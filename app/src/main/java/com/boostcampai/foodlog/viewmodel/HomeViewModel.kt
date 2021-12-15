package com.boostcampai.foodlog.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boostcampai.foodlog.FoodLogApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val goal = FoodLogApplication.goalValuePreference
    val unit = FoodLogApplication.goalUnitPreference

    var date = getCurrentDate()
    var current: MutableLiveData<Int> = MutableLiveData<Int>(100)

    @SuppressLint("NewApi")
    fun getCurrentDate() = LocalDate.now().toString()
}
