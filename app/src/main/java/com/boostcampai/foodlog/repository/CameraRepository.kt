package com.boostcampai.foodlog.repository

import com.boostcampai.foodlog.data.dao.DietDao
import com.boostcampai.foodlog.data.dao.FoodDao
import com.boostcampai.foodlog.di.RemoteSource
import com.boostcampai.foodlog.network.InferenceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraRepository @Inject constructor(
    @RemoteSource val inferenceSource: InferenceSource,
    private val dietDao: DietDao,
    private val foodDao: FoodDao
) {
    suspend fun getImageInference(): Result<InferenceResponse> =
        withContext(Dispatchers.IO) { inferenceSource.getInferenceResponse() }
}
