package com.android.games.bigjoker.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.android.games.bigjoker.App

class Prefs {

    private val preferences: SharedPreferences =
        App.appContext.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE)

    fun saveLink(link: String) {
        setLink(link, LINK_KEY)
    }

    fun getLink(): String? {
        return getLink(LINK_KEY)
    }

    private fun getLink(key: String, default: String = "null"): String? {
        return preferences.getString(key, default)
    }

    private fun setLink(value: String, key: String) {
        preferences.edit().putString(key, value).apply()
    }

    companion object {
        private const val NAME_SP = "AppShared"
        private const val LINK_KEY = "link"
    }
}