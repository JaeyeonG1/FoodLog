package com.boostcampai.foodlog.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ImageInferenceService {
    @GET("api/v1/inference")
    fun requestInferenceResult(): Call<InferenceResponse>

    companion object {
        private const val apiUrl = "http://49.50.166.29:6011/"

        fun create(): ImageInferenceService {
            return Retrofit
                .Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ImageInferenceService::class.java)
        }
    }
}
