package com.im30.ROE.g.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.im30.ROE.g.App

class Prefs {
    private val preferences: SharedPreferences =
        App.appContext.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE)

    fun saveLink(link: String) {
        setLink(link, LINK_KEY)
    }

    fun getLink(): String? {
        return getLink(LINK_KEY)
    }

    fun saveOpenNumber(number: Int) {
        setNumber(number, NUMBER_OF_LINK)
    }

    fun getOpenNumber(): Int? {
        return getNumber(NUMBER_OF_LINK)
    }

    private fun getLink(key: String, default: String = "null"): String? {
        return preferences.getString(key, default)
    }

    private fun setLink(value: String, key: String) {
        preferences.edit().putString(key, value).apply()
    }


    private fun getNumber(key: String, default: Int = 1): Int? {
        return preferences.getInt(key, default)
    }

    private fun setNumber(value: Int, key: String) {
        preferences.edit().putInt(key, value).apply()
    }

    companion object {
        private const val NAME_SP = "AppShared"
        private const val LINK_KEY = "link"
        private const val NUMBER_OF_LINK = "number"
    }
}