package com.boostcampai.foodlog.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Food")
@Parcelize
data class Food(
    var name: String,
    var cls: Int,
    var pos: Position,
    var kcal: Float,
    var dan: Float,
    var zi: Float,
    var tan: Float,
    var imgId: Long,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
) : Parcelable {
    fun getValueByUnit(unit: String): Float {
        return when (unit) {
            "kcal" -> { kcal }
            "tan" -> { tan }
            "dan" -> { dan }
            "zi" -> {zi}
//            "na" -> {}
            else -> { 0f }
        }
    }

    fun getValueStringByUnit() {

    }
}

fun List<Food>.total(unit: String): Float {
    var temp = when (unit) {
        "kcal" -> {
            this.fold(0f) { sum, now ->
                sum + now.kcal
            }
        }
        "tan" -> {
            this.fold(0f) { sum, now ->
                sum + now.tan
            }
        }
        "dan" -> {
            this.fold(0f) { sum, now ->
                sum + now.dan
            }
        }
        "zi" -> {
            this.fold(0f) { sum, now ->
                sum + now.zi
            }
        }
//        "na" -> {
//            this.fold(0f) { sum, now ->
//                sum + now.na
//            }
//        }
        else -> {
            0f
        }
    }

    return temp
}