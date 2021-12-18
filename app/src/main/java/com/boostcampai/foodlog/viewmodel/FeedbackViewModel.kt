package com.boostcampai.foodlog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boostcampai.foodlog.model.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor() : ViewModel() {
    private var _foodWithStatus = MutableLiveData<MutableList<Pair<Food, Boolean>>>()
    val foodWithStatus: LiveData<MutableList<Pair<Food, Boolean>>> = _foodWithStatus

    fun loadFoods(foods: List<Food>) {
        _foodWithStatus.value = foods.map { Pair(it, true) }.toMutableList()
    }

    fun applyStatus(pos: Int) {
        _foodWithStatus.value = _foodWithStatus.value?.toMutableList().apply {
            if (this != null) {
                this[pos] = Pair(this[pos].first, !this[pos].second)
            }
        }
    }
}
