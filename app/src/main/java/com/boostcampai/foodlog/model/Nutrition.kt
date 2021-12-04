package com.boostcampai.foodlog.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Nutrition(
    var unit:String,
    var value:Float
) : Parcelable
