package com.serhatd.streamtapedownloader.data.prefs

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs {
    companion object {
        fun getSharedPreference(context: Context, key: String): String {
            return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(key, "").toString()
        }

        fun setSharedPreference(context: Context, key: String, value: String) {
            val sharedPref: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            val edit = sharedPref.edit()
            edit.putString(key, value)
            edit.apply()
        }
    }
}