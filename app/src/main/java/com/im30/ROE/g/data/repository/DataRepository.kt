package com.im30.ROE.g.data.repository

interface DataRepository {
    fun getFullLink(): String?
    fun saveFullLink(link: String)
    fun getCurrentOpenNumber(): Int?
    fun setCurrentOpenNumber(number: Int)
}