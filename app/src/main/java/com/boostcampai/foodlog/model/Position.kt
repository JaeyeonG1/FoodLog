package com.boostcampai.foodlog.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Position(
    var topLeftX:Float,
    var topLeftY:Float,
    var bottomRightX:Float,
    var bottomRightY:Float
) : Parcelable
