package com.boostcampai.foodlog.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InferenceResponse(
    @SerializedName("Diet")
    val diet: DietResponse = DietResponse()
) : Parcelable

@Parcelize
data class DietResponse(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("foods")
    val foods: List<FoodResponse> = listOf()
) : Parcelable

@Parcelize
data class FoodResponse(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("cls")
    val cls: Int = 0,
    @SerializedName("pos")
    val pos: List<Float> = listOf(),
    @SerializedName("kcal")
    val kcal: Float = 0f,
    @SerializedName("단백질")
    val dan: Float = 0f,
    @SerializedName("지방")
    val zi: Float = 0f,
    @SerializedName("탄수화물")
    val tan: Float = 0f
) : Parcelable
