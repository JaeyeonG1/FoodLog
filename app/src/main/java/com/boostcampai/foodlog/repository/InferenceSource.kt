package com.boostcampai.foodlog.repository

import com.boostcampai.foodlog.di.RemoteSource
import com.boostcampai.foodlog.network.FoodResponse
import com.boostcampai.foodlog.network.ImageInferenceService
import com.boostcampai.foodlog.network.InferenceResponse
import com.boostcampai.foodlog.network.callSafeApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

interface InferenceSource {
    suspend fun getInferenceResponse(
        base64: String,
        dateTime: String
    ): Result<InferenceResponse>

    suspend fun sendFeedback(
        base64: String, dateTime: String, foodList: List<FoodResponse>, feedback: List<Boolean>
    ): Result<String>
}

class RemoteInferenceSource @Inject constructor(
    private val inferenceService: ImageInferenceService
) : InferenceSource {
    override suspend fun getInferenceResponse(
        base64: String,
        dateTime: String
    ): Result<InferenceResponse> =
        callSafeApi {
            hashMapOf(Pair("img", base64), Pair("date_time", dateTime))
            inferenceService.requestInferenceResult(
                hashMapOf(
                    Pair("img", base64),
                    Pair("date_time", dateTime)
                )
            ).execute()
        }

    override suspend fun sendFeedback(
        base64: String,
        dateTime: String,
        foodList: List<FoodResponse>,
        feedback: List<Boolean>
    ): Result<String> =
        callSafeApi {
            inferenceService.sendFeedback(
                hashMapOf(
                    Pair("img", base64),
                    Pair("date_time", dateTime),
                    Pair("food_list", foodList),
                    Pair("feedback", feedback)
                )
            ).execute()
        }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class InferenceSourceModule {
    @Binds
    @RemoteSource
    @Singleton
    abstract fun provideRemoteInferenceSource(remoteInferenceSource: RemoteInferenceSource): InferenceSource
}
