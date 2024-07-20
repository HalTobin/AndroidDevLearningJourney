package com.chapeaumoineau.pocketlocker.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SecretPreferenceRepository @Inject constructor(private val sp: SharedPreferences) {

    companion object Keys {
        const val PIN = "pin"
    }

    fun setPin(pin: String) = sp.edit {
        putString(PIN, pin)
        apply()
    }

    fun isPinValid(pin: String): Boolean = sp.getString(PIN, "") == pin

    fun isPinExisting(): Boolean = sp.getString(PIN, null) != null

}