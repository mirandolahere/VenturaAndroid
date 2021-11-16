package com.application.venturaapp.preference

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    private val PREF_NAME = "com.delaware.ventura"
    internal var editor: SharedPreferences.Editor? = null
    internal var pref: SharedPreferences? = null

    init {
        if (pref == null) {
            pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            editor = pref!!.edit()
        }
    }

    fun saveString(key: String, value: String) {
        editor!!.putString(key, value)
        editor!!.commit()
    }

    fun getString(key: String): String? {
        return pref!!.getString(key, null)
    }

    fun deleteKey(key: String) {
        editor!!.remove(key)
        editor!!.apply()
    }

    fun contains(key: String): Boolean {
        return pref!!.contains(key)
    }
}