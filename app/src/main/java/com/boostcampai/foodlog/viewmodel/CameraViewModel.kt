package com.boostcampai.foodlog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcampai.foodlog.model.Diet
import com.boostcampai.foodlog.network.InferenceResponse
import com.boostcampai.foodlog.repository.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraRepository: CameraRepository
) : ViewModel() {
    private var _inferenceResult = MutableLiveData<InferenceResponse>()
    val inferenceResult: LiveData<InferenceResponse> = _inferenceResult

    fun inferenceResult() {
        CoroutineScope(Dispatchers.IO).launch {
            cameraRepository.getImageInference()
                .onSuccess {
                    viewModelScope.launch { _inferenceResult.value = it }
                }.onFailure {
                    Log.d("getImageInferenceResult", "Failure")
                }
        }
    }

    fun saveResult() {
        val dateTime = inferenceResult.value?.diet?.date
        val foods = inferenceResult.value?.diet?.foods
        val uri = " "

        CoroutineScope(Dispatchers.IO).launch {
            dateTime?.let { Diet(uri = uri, dateTime = it) }
                ?.let { cameraRepository.dietDao.insert(it) }
        }
    }
}
