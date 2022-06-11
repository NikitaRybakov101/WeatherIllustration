package com.example.weatherpaint.data.sharedPreference

import android.content.Context
import com.example.weatherpaint.ui.activity.MainActivity
import com.example.weatherpaint.ui.fragments.other.Const.START_DATA_LOCATIONS

object SharedPreference {

    fun saveDataLocation(stringData: String, key: String, activity: MainActivity) {
        val editor = activity.getPreferences(Context.MODE_PRIVATE).edit()

        editor.putString(key, stringData)
        editor.apply()
    }

    fun saveDataBoolean(boolean: Boolean, key: String, activity: MainActivity) {
        val editor = activity.getPreferences(Context.MODE_PRIVATE).edit()

        editor.putBoolean(key, boolean)
        editor.apply()
    }

    fun loadDataCity(key: String, activity: MainActivity) = activity.getPreferences(Context.MODE_PRIVATE).getString(key, START_DATA_LOCATIONS)

    fun loadDataBoolean(key: String, activity: MainActivity) = activity.getPreferences(Context.MODE_PRIVATE).getBoolean(key, false)

    fun loadDataBooleanDefTrue(key: String, activity: MainActivity) = activity.getPreferences(Context.MODE_PRIVATE).getBoolean(key, true)
}