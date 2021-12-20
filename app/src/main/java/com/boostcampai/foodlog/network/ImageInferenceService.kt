package com.boostcampai.foodlog.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ImageInferenceService {
    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("api/v1/inference")
    fun requestInferenceResult(
        @Body body: HashMap<String, String>
    ): Call<InferenceResponse>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("api/v1/feedback")
    fun sendFeedback(
        @Body body: HashMap<String, Any>
    ): Call<String>

    companion object {
        private const val apiUrl = "http://121.182.238.97:8000/"

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
