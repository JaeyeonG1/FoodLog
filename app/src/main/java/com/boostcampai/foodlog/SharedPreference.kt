package com.boostcampai.foodlog

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Preference<T>(
    open val prefs: SharedPreferences, open val key: String
) : ReadWriteProperty<Any, T>

private class PreferenceLiveData<T>(
    private val preference: Preference<T>
) : LiveData<T>() {
    private val delegateValue: T by preference
    private val listener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (preference.key == key) {
                value = delegateValue
            }
        }

    override fun onActive() {
        super.onActive()
        value = delegateValue
        preference.prefs
            .registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onInactive() {
        preference.prefs
            .unregisterOnSharedPreferenceChangeListener(listener)
        super.onInactive()
    }
}

fun <T> Preference<T>.asLiveData(): LiveData<T> {
    return PreferenceLiveData(this)
}

class IntPreference(
    override val prefs: SharedPreferences,
    override val key: String,
    private val defaultValue: Int = 2000
) : Preference<Int>(prefs, key) {
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return prefs.getInt(key, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
}

class StringPreference(
    override val prefs: SharedPreferences,
    override val key: String,
    private val defaultValue: String = "kcal"
) : Preference<String>(prefs, key) {
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}
