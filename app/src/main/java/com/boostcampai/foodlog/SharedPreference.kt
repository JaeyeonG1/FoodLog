package com.boostcampai.foodlog

import android.content.Context

class SharedPreference(context: Context) {

    private val prefsFilename = "prefs"
    private val prefs = context.getSharedPreferences(prefsFilename, 0)

    var goalValue: Int
        get() = prefs.getInt("goal", 2000)
        set(value) = prefs.edit().putInt("goal", value).apply()

    var goalUnit: String
        get() = prefs.getString("unit", "kcal") ?: "kcal"
        set(value) = prefs.edit().putString("goal", value).apply()
}
