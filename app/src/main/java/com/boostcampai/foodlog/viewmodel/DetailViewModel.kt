package com.boostcampai.foodlog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcampai.foodlog.FoodLogApplication
import com.boostcampai.foodlog.data.dao.DietWithFoods
import com.boostcampai.foodlog.model.DailyDietModel
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.repository.DailyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.forEach
import kotlin.collections.hashMapOf
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.set
import kotlin.collections.toMutableList

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val dailyRepository: DailyRepository
) : ViewModel() {
    val goal: LiveData<Int> = FoodLogApplication.goalValuePreference
    val unit: LiveData<String> = FoodLogApplication.goalUnitPreference
    val dietWithFoods = MediatorLiveData<List<DietWithFoods>>()
    private val _dailyDietModels = MutableLiveData<List<DailyDietModel>>()
    val dailyDietModels: LiveData<List<DailyDietModel>> = _dailyDietModels

    init {
        loadDailyDiet()
    }

    var date = getCurrentDate()

    private fun getCurrentDate() = LocalDate.now().toString()
    fun loadDietWithFoods(date: String): List<DietWithFoods> {
        val list = mutableListOf<DietWithFoods>()
        dietWithFoods.value?.forEach {
            if (it.diet.dateTime.contains(date)) {
                list.add(it)
                Log.d("load", it.toString())
            }
        }
        return list
    }

    private fun loadDailyDiet() {
        viewModelScope.launch {
            dietWithFoods.addSource(dailyRepository.loadDiet()) { list ->
                dietWithFoods.value = list
                val map = hashMapOf<String, MutableList<Food>>()
                list.forEach {
                    map[it.diet.dateTime.split(" ")[0]].let { _ ->
                        map[it.diet.dateTime.split(" ")[0]]?.addAll(it.foods)
                    } ?: run { map[it.diet.dateTime.split(" ")[0]] = it.foods.toMutableList() }
                }
                _dailyDietModels.value = map.map {
                    DailyDietModel(
                        it.key,
                        goal.value ?: 0,
                        unit.value ?: "kcal",
                        it.value
                    )
                }
            }
        }
    }
}
