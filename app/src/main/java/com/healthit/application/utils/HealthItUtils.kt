package com.healthit.application.utils

import android.os.Build
import com.google.gson.Gson
import java.util.*
import android.util.Base64.*
import java.io.UnsupportedEncodingException

class HealthItUtils {
    companion object {

        private const val CVC_LENGTH_MAX: Int = 4
        private const val CVC_LENGTH_MIN: Int = 3

        fun <T> fromJson(json: String?, classOfT: Class<T>): T? {
            if (json == null)
                return null
            return Gson().fromJson(json, classOfT)
        }

        fun decodeString(encoded: String): String {
            val dataDec: ByteArray? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getDecoder().decode(encoded)
            } else {
                decode(encoded, DEFAULT)
            }

            var decodedString = ""
            try {
                decodedString = String(dataDec!!)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            return decodedString
        }

        fun encodeString(decoded: String): String {
            var decodedString = ""
            decodedString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().withoutPadding().encodeToString(decoded.toByteArray())
            } else {
                decoded
            }
            return decodedString
        }

        val generateUniqueId: String
            get() = UUID.randomUUID().toString()
    }
}