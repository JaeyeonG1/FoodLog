package com.boostcampai.foodlog.repository

import com.boostcampai.foodlog.di.RemoteSource
import com.boostcampai.foodlog.network.ImageInferenceService
import com.boostcampai.foodlog.network.InferenceResponse
import com.boostcampai.foodlog.network.callSafeApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

interface InferenceSource {
    suspend fun getInferenceResponse(): Result<InferenceResponse>
}

class RemoteInferenceSource @Inject constructor(
    private val inferenceService: ImageInferenceService
) : InferenceSource {
    override suspend fun getInferenceResponse(): Result<InferenceResponse> =
        callSafeApi { inferenceService.requestInferenceResult().execute() }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class InferenceSourceModule {
    @Binds
    @RemoteSource
    @Singleton
    abstract fun provideRemoteInferenceSource(remoteInferenceSource: RemoteInferenceSource): InferenceSource
}
