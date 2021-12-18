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
    var na: Float,
    var imgId: Long,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
) : Parcelable {
    fun getValueByUnit(unit: String): Float {
        return when (unit) {
            "kcal" -> {
                kcal
            }
            "tan" -> {
                tan
            }
            "dan" -> {
                dan
            }
            "zi" -> {
                zi
            }
            "na" -> {
                na
            }
            else -> {
                0f
            }
        }
    }

    fun getValueStringByUnit() {

    }
}

fun List<Food>.total(unit: String): Float {
    return this.fold(0f) { sum, now ->
        sum + when (unit) {
            "kcal" -> now.kcal
            "tan" -> now.tan
            "dan" -> now.dan
            "zi" -> now.zi
            "na" -> now.na
            else -> 0f
        }
    }
}

fun List<Food>.subUnits(unit: String): Triple<List<String>, List<Float>, List<String>> {
    val names = mutableListOf("칼로리", "탄수화물", "단백질", "지방", "나트륨")
    val units = mutableListOf("kcal", "g", "g", "g", "mg")
    val values = when (unit) {
        "kcal" -> {
            names.removeAt(0)
            units.removeAt(0)
            this.fold(mutableListOf(0f, 0f, 0f, 0f)) { sum, now ->
                mutableListOf(sum[0] + now.tan, sum[1] + now.dan, sum[2] + now.zi, sum[3] + now.na)
            }
        }
        "tan" -> {
            names.removeAt(1)
            units.removeAt(1)
            this.fold(mutableListOf(0f, 0f, 0f, 0f)) { sum, now ->
                mutableListOf(sum[0] + now.kcal, sum[1] + now.dan, sum[2] + now.zi, sum[3] + now.na)
            }
        }
        "dan" -> {
            names.removeAt(2)
            units.removeAt(2)
            this.fold(mutableListOf(0f, 0f, 0f, 0f)) { sum, now ->
                mutableListOf(sum[0] + now.kcal, sum[1] + now.tan, sum[2] + now.zi, sum[3] + now.na)
            }
        }
        "zi" -> {
            names.removeAt(3)
            units.removeAt(3)
            this.fold(mutableListOf(0f, 0f, 0f, 0f)) { sum, now ->
                mutableListOf(sum[0] + now.kcal, sum[1] + now.tan, sum[2] + now.dan, sum[3] + now.na)
            }
        }
        "na" -> {
            names.removeAt(4)
            units.removeAt(4)
            this.fold(mutableListOf(0f, 0f, 0f, 0f)) { sum, now ->
                mutableListOf(sum[0] + now.kcal, sum[1] + now.tan, sum[2] + now.dan, sum[3] + now.zi)
            }
        }
        else -> mutableListOf(0f, 0f, 0f, 0f)
    }

    return Triple(names, values, units)
}
