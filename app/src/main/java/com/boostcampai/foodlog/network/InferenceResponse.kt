package com.boostcampai.foodlog.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InferenceResponse(
    @SerializedName("Diet")
    val Diet: DietResponse = DietResponse()
) : Parcelable

@Parcelize
data class DietResponse(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("foods")
    val foods: List<FoodResponse> = listOf()
):Parcelable

@Parcelize
data class FoodResponse(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("cls")
    val cls: String = "",
    @SerializedName("pos")
    val pos: List<Float> = listOf(),
    @SerializedName("kcal")
    val kcal: Int = 0,
    @SerializedName("단백질")
    val dan: Double = 0.0,
    @SerializedName("지방")
    val zi: Double = 0.0,
    @SerializedName("탄수화뭃")
    val tan: Double = 0.0
):Parcelable
