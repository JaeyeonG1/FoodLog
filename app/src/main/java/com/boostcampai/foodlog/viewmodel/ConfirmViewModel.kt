package com.boostcampai.foodlog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.model.Position
import com.boostcampai.foodlog.network.DietResponse
import com.boostcampai.foodlog.network.FoodResponse
import com.boostcampai.foodlog.network.InferenceResponse
import com.boostcampai.foodlog.repository.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    private val cameraRepository: CameraRepository
) : ViewModel() {
    private var _inferenceResult = MutableLiveData<InferenceResponse>()
    val inferenceResult: LiveData<InferenceResponse> = _inferenceResult

    var imgBase64: String = ""

    fun inferenceFromBitmap(
        onStart: () -> (Unit),
        onSuccess: (DietResponse) -> (Unit),
        onFinish: () -> (Unit)
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.Main).launch { onStart() }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
            cameraRepository.getInferenceResult(imgBase64, LocalDateTime.now().format(formatter))
                .onSuccess {
                    viewModelScope.launch {
                        _inferenceResult.value = it
                        Log.d("Result", it.toString())
                        CoroutineScope(Dispatchers.Main).launch { onFinish() }
                        onSuccess(it.diet)
                    }
                }.onFailure {
                    Log.d("getImageInferenceResult", "Failure")
                    CoroutineScope(Dispatchers.Main).launch { onFinish() }
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
