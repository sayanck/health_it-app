package com.healthit.application.data


object DataManager {
    private var cookies: ArrayList<String> = arrayListOf()

    fun clear() {
        cookies = java.util.ArrayList()
    }
}