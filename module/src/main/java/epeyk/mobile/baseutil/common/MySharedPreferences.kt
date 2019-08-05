package epeyk.mobile.baseutil.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class MySharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }


    fun saveToPreferences(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveToPreferences(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getFromPreferences(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

}
