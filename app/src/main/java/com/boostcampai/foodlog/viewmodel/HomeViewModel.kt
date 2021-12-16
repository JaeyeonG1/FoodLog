package com.boostcampai.foodlog.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcampai.foodlog.FoodLogApplication
import com.boostcampai.foodlog.data.dao.DietWithFoods
import com.boostcampai.foodlog.model.TodayDietModel
import com.boostcampai.foodlog.model.total
import com.boostcampai.foodlog.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    val goal: LiveData<Int> = FoodLogApplication.goalValuePreference
    val unit: LiveData<String> = FoodLogApplication.goalUnitPreference
    val dietWithFoods = MediatorLiveData<List<DietWithFoods>>()
    private val _todayDietModels = MutableLiveData<List<TodayDietModel>>()
    val todayDietModels: LiveData<List<TodayDietModel>> = _todayDietModels

    var date = getCurrentDate()
    var presentDate = getDate(date)
    var current: MutableLiveData<Int> = MutableLiveData<Int>(0)
//    var current:MutableList<Int> = MutableLiveData<Int>()
    init {
        loadDailyDiets()
    }

    @SuppressLint("NewApi")
    fun getCurrentDate() = LocalDate.now().toString()

    private fun loadDailyDiets() {
        viewModelScope.launch {
            val temp = homeRepository.loadDiet(date)
            dietWithFoods.addSource(temp) { list ->
//                Log.d("loadDailyDiets", "Changed")
                _todayDietModels.value = list.map {
                    TodayDietModel(getTime(it.diet.dateTime), it.foods)
                }
                current.value = list.map{
                    it.foods.total(unit.value?:"kcal")
                }.sum().toInt()
            }
        }
    }

    private fun getDate(date: String): String {
        // String form is 'YYYY-MM-DD HH:MM:SS'
        var temp = date.split(" ")[0].split("-")

        return temp[1] + "월 " + temp[2] + "일"
    }
    private fun getTime(date: String): String{
        // String form is 'YYYY-MM-DD HH:MM:SS'
        var temp = date.split(" ")[1].split(":")

        return temp[0] + ":" + temp[1]
    }
}
