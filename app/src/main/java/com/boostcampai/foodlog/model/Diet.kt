package com.boostcampai.foodlog.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Diet")
data class Diet(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var uri: String,
    var dateTime: String
)
