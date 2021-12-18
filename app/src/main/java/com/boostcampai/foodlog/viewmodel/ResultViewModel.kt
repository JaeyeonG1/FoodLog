package com.boostcampai.foodlog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boostcampai.foodlog.FoodLogApplication
import com.boostcampai.foodlog.model.Diet
import com.boostcampai.foodlog.model.InferenceResult
import com.boostcampai.foodlog.network.DietResponse
import com.boostcampai.foodlog.repository.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val cameraRepository: CameraRepository
) : ViewModel() {
    val goal: LiveData<Int> = FoodLogApplication.goalValuePreference
    val unit: LiveData<String> = FoodLogApplication.goalUnitPreference

    private var _inferenceResult = MutableLiveData<InferenceResult>()
    val inferenceResult: LiveData<InferenceResult> = _inferenceResult

    fun loadInferenceResult(result: DietResponse) {
        _inferenceResult.value = InferenceResult(
            result.date.split(" ")[0],
            result.date.split(" ")[1],
            result.status,
            result.foods.map { it.convertToFood(0L) }
        )
    }

    fun removeFood(pos: Int) {
        _inferenceResult.value = _inferenceResult.value?.apply {
            foods = foods.toMutableList().apply {
                removeAt(pos)
            }
        }
    }

    fun saveResult(uri: String) {
        if (inferenceResult.value == null)
            return

        val dateTime = inferenceResult.value!!.run { "$date $time" }
        val foods = inferenceResult.value!!.foods

        CoroutineScope(Dispatchers.IO).launch {
            with(Diet(uri = uri, dateTime = dateTime)) {
                val id = cameraRepository.dietDao.insert(this)
                with(foods.map { food -> food.apply { imgId = id[0] } }) {
                    cameraRepository.foodDao.insert(*this.toTypedArray())
                }
            }
        }
    }
}
