package com.android.games.bigjoker.utils

import android.content.Context
import android.provider.Settings
import java.io.File

class CheckBlock(private val context: Context) {

    fun isBlock():Boolean {
        return (checkRoot() || checkADB())
    }

    private fun checkRoot(): Boolean {
        val array = arrayOf(
            "/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/",
            "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"
        )
        try {
            for (dir in array) {
                if (File(dir + "su").exists()) return true
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return false
    }

    private fun checkADB(): Boolean {
        return Settings.Global.getString(
            context.contentResolver,
            Settings.Global.ADB_ENABLED) == "1"
    }
}