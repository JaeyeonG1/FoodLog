package com.boostcampai.foodlog.repository

import com.boostcampai.foodlog.data.dao.DietDao
import com.boostcampai.foodlog.data.dao.FoodDao
import com.boostcampai.foodlog.network.ImageInferenceService
import com.boostcampai.foodlog.network.InferenceResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraRepository @Inject constructor(
    private val imageInferenceService: ImageInferenceService,
    private val dietDao: DietDao,
    private val foodDao: FoodDao
) {
    fun getImageInference(): Result<InferenceResponse> {
        return Result.success(imageInferenceService.requestInferenceResult().execute().body()!!)
    }
}
