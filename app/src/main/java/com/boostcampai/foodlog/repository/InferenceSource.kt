package com.boostcampai.foodlog.repository

import android.util.Log
import com.boostcampai.foodlog.di.RemoteSource
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
            inferenceService.requestInferenceResult(hashMapOf(Pair("img", base64), Pair("date_time", dateTime))).execute()
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
