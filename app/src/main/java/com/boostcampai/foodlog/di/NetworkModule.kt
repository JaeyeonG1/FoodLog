package com.boostcampai.foodlog.di

import com.boostcampai.foodlog.network.ImageInferenceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideImageInferenceService(): ImageInferenceService{
        return ImageInferenceService.create()
    }
}
