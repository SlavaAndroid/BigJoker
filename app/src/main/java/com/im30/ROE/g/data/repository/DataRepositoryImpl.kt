package com.im30.ROE.g.data.repository

import com.im30.ROE.g.data.prefs.Prefs

class DataRepositoryImpl : DataRepository {

    private val prefs = Prefs()

    override fun getFullLink(): String? {
        return prefs.getLink()
    }

    override fun saveFullLink(link: String) {
        prefs.saveLink(link)
    }

    override fun getCurrentOpenNumber(): Int? {
        return prefs.getOpenNumber()
    }

    override fun setCurrentOpenNumber(number: Int) {
        prefs.saveOpenNumber(number)
    }
}