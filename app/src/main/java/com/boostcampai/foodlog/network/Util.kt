package com.boostcampai.foodlog.network

import android.util.Log
import retrofit2.Response

suspend fun <T : Any> callSafeApi(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            if (response.body() != null) {
                if (response.body() is Iterable<*> &&
                    (response.body() as Iterable<*>).none()
                ) {
                    Log.d("callSafeApi", "Empty Data!")
                    Result.failure<T>(Exception("Empty Data!"))
                }
                Log.d("callSafeApi", "Success")
                Result.success(response.body()!!)
            } else {
                Log.d("callSafeApi", "Data Load Failed")
                Result.failure(Exception("Data Load Failed"))
            }
        } else {
            Log.d("callSafeApi", "Failure")
            Result.failure(Exception(response.message()))
        }

    } catch (e: Exception) {
        Log.d("callSafeApi", "Network Error")
        Result.failure(Exception(e.message ?: "Network Error"))
    }
}
