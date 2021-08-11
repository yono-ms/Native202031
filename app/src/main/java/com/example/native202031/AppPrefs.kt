package com.example.native202031

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class AppPrefs(context: Context) {

    enum class PrefKey {
        USER_NAME,
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getUserName(): String? {
        return prefs.getString(PrefKey.USER_NAME.name, null)
    }

    fun setUserName(userName: String) {
        prefs.edit { putString(PrefKey.USER_NAME.name, userName) }
    }
}
