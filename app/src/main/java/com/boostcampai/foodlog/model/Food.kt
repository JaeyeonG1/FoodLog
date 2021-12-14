package com.boostcampai.foodlog.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food")
data class Food(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var imgId: Long,
    var cls: Int,
    var name: String,
    var pos: Position,
    var nutrition: Nutrition
)
