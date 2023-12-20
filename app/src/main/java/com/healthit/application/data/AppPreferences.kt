package com.healthit.application.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.healthit.application.model.response.userDetails.User
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.constant.PrefConstant.FCM_TOKEN
import com.healthit.application.utils.constant.PrefConstant.LOGGED_IN
import com.healthit.application.utils.constant.PrefConstant.SELECTED_LANGUAGE
import com.healthit.application.utils.constant.PrefConstant.USER_DETAIL
import com.healthit.application.utils.constant.PrefConstant.USER_TYPE

object AppPreferences {

    private const val NAME = "PREF_SHARED_PREFS_NAME"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            NAME, MODE
        )
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var selectedLanguage: String
        get() = preferences.getString(SELECTED_LANGUAGE, HelperConstant.ENGLISH)
            ?: HelperConstant.ENGLISH
        set(value) = preferences.edit {
            it.putString(SELECTED_LANGUAGE, value)
            it.commit()
        }

    var isLoggedIn: Boolean
        get() = preferences.getBoolean(LOGGED_IN, false)
        set(value) = preferences.edit {
            it.putBoolean(LOGGED_IN, value)
            it.commit()
        }

    var fcmToken: String
        get() = preferences.getString(FCM_TOKEN, "") ?: ""
        set(value) = preferences.edit {
            it.putString(FCM_TOKEN, value)
            it.commit()
        }

    var userDetails: User?
        get() = Gson().fromJson(preferences.getString(USER_DETAIL, "") ?: "", User::class.java)
        set(value) = preferences.edit {
            it.putString(USER_DETAIL, Gson().toJson(value, User::class.java))
            it.commit()
        }

    var userType: String
        get() = preferences.getString(USER_TYPE, "") ?: ""
        set(value) = preferences.edit {
            it.putString(USER_TYPE, value)
            it.commit()
        }
}