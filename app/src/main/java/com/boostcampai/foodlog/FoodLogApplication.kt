package com.boostcampai.foodlog

import android.app.Application
import androidx.lifecycle.LiveData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodLogApplication : Application() {
    companion object {
        lateinit var goalValuePreference: LiveData<Int>
        lateinit var goalUnitPreference: LiveData<String>
    }

    private val prefsFileName = "prefs"

    override fun onCreate() {
        goalValuePreference =
            IntPreference(
                applicationContext.getSharedPreferences(prefsFileName, 0),
                "goalValue"
            ).asLiveData()
        goalUnitPreference =
            StringPreference(
                applicationContext.getSharedPreferences(prefsFileName, 0),
                "goalUnit"
            ).asLiveData()
        super.onCreate()
    }
}
