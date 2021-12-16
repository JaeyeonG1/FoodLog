package com.boostcampai.foodlog.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Diet")
@Parcelize
data class Diet(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var uri: String,
    var dateTime: String
) : Parcelable
