package com.android.games.bigjoker.data.repository

import com.android.games.bigjoker.data.prefs.Prefs

class DataRepositoryImpl : DataRepository {

    private val prefs = Prefs()

    override fun getFullLink(): String? {
        return prefs.getLink()
    }

    override fun saveFullLink(link: String) {
        prefs.saveLink(link)
    }
}