package com.healthit.application.utils

import com.healthit.application.BuildConfig
import java.io.OutputStream

object Logger {
    internal var output: OutputStream = System.out
    internal var error: OutputStream = System.err
    internal var isEnabled = BuildConfig.DEBUG
}

fun Any.logD(message: String) {
    if (Logger.isEnabled) {
        Logger.output.write("${this::class.simpleName}: $message\n".toByteArray())
    }
}

inline fun Any.logD(crossinline messageProducer: () -> String) {
    logD(messageProducer())
}