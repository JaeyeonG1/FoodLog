package com.boostcampai.foodlog.network

import android.os.Parcelable
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.model.Position
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InferenceResponse(
    @SerializedName("diet")
    val diet: DietResponse = DietResponse()
) : Parcelable

@Parcelize
data class DietResponse(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("date_time")
    val date: String = "",
    @SerializedName("food_list")
    val foods: List<FoodResponse> = listOf()
) : Parcelable

@Parcelize
data class FoodResponse(
    @SerializedName("name_ko")
    val name: String = "",
    @SerializedName("cls")
    val cls: Int = 0,
    @SerializedName("bbox")
    val pos: List<Float> = listOf(),
    @SerializedName("kcal")
    val kcal: Float = 0f,
    @SerializedName("dan")
    val dan: Float = 0f,
    @SerializedName("gi")
    val zi: Float = 0f,
    @SerializedName("tan")
    val tan: Float = 0f,
    @SerializedName("name")
    val nameEng: String,
    @SerializedName("na")
    val na: Float,
    @SerializedName("serving_size")
    val servingSize: String,
) : Parcelable {
    fun convertToFood(imgId: Long): Food {
        return Food(
            name, cls, Position(pos[0], pos[1], pos[2], pos[3]), kcal, dan, zi, tan, na, imgId
        )
    }
}
