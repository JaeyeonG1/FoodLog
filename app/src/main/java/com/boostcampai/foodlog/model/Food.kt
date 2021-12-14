package com.boostcampai.foodlog.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food")
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
)
