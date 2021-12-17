package com.boostcampai.foodlog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcampai.foodlog.model.Diet
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.model.Position
import com.boostcampai.foodlog.network.FoodResponse
import com.boostcampai.foodlog.network.InferenceResponse
import com.boostcampai.foodlog.repository.CameraRepository
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    private val cameraRepository: CameraRepository
) : ViewModel() {
    private var _inferenceResult = MutableLiveData<InferenceResponse>()
    val inferenceResult: LiveData<InferenceResponse> = _inferenceResult

    fun inferenceFromBitmap(base64Str: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
            cameraRepository.getInferenceResult(base64Str, LocalDateTime.now().format(formatter))
                .onSuccess {
                    viewModelScope.launch {
                        _inferenceResult.value = it
                        Log.d("Result", it.toString())}
                }.onFailure {
                    Log.d("getImageInferenceResult", "Failure")
                }
        }
    }

    fun saveResult() {
        if (inferenceResult.value == null)
            return

        val dateTime = inferenceResult.value!!.diet.date
        val foods = inferenceResult.value!!.diet.foods
        val uri = "uri"

        CoroutineScope(Dispatchers.IO).launch {
            Diet(uri = uri, dateTime = dateTime)
                .let {
                    val id = cameraRepository.dietDao.insert(it)
                    foods.map { foodResponse ->
                        foodResponseToFood(foodResponse, id[0])
                    }.let { food -> cameraRepository.foodDao.insert(*food.toTypedArray()) }
                }
        }
    }

    private fun foodResponseToFood(food: FoodResponse, imgId: Long) = Food(
        food.name,
        food.cls,
        Position(food.pos[0], food.pos[1], food.pos[2], food.pos[3]),
        food.kcal,
        food.dan,
        food.zi,
        food.tan,
        food.na,
        imgId
    )
}
