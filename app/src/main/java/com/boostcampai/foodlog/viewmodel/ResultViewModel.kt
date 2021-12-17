package com.boostcampai.foodlog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.boostcampai.foodlog.FoodLogApplication
import com.boostcampai.foodlog.model.InferenceResult
import com.boostcampai.foodlog.network.DietResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel() {
    val goal: LiveData<Int> = FoodLogApplication.goalValuePreference
    val unit: LiveData<String> = FoodLogApplication.goalUnitPreference

    fun loadInferenceResult(result: DietResponse): InferenceResult {
        return InferenceResult(
            result.date.split(" ")[0],
            result.date.split(" ")[1],
            result.status,
            result.foods.map { it.convertToFood(0L) }
        )
    }
}
