package com.android.games.bigjoker.data.repository

interface DataRepository {
    fun getFullLink(): String?
    fun saveFullLink(link: String)
}