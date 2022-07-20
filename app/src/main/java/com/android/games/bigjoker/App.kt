package com.android.games.bigjoker

import android.app.Application
import android.content.Context
import com.android.games.bigjoker.utils.Params.ONESIGNAL_APP_ID
import com.onesignal.OneSignal

class App : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

    }
}