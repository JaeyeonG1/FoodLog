package com.boostcampai.foodlog

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodLogApplication : Application() {
    companion object {
        lateinit var prefs: SharedPreference
    }

    override fun onCreate() {
        prefs = SharedPreference(applicationContext)
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
