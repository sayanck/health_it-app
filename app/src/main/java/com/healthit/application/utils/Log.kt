package com.healthit.application.utils

import com.healthit.application.BuildConfig


object Log {

    val log: Boolean = BuildConfig.DEBUG

    fun i(tag: String?, string: String?) {
        if (log) Log.i(tag, string!!)
    }

    fun e(tag: String?, string: String?) {
        if (log) Log.e(tag, string!!)
    }

    fun d(tag: String?, string: String?) {
        if (log) Log.d(tag, string!!)
    }

    fun v(tag: String?, string: String?) {
        if (log) Log.v(tag, string!!)
    }
}