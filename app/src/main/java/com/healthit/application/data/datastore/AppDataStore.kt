package com.healthit.application.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.healthit.application.model.response.loginRegister.CustomerDetails
import com.healthit.application.utils.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class AppDataStore(private val context: Context) {

    private val Context.prefDataStore: DataStore<Preferences> by preferencesDataStore(NAME)

    companion object {
        private const val NAME = "PREF_SHARED_PREFS_NAME"
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val IS_FINISH_ONBOARD = booleanPreferencesKey("is_finish_onboard")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    var isLoggedIn: Flow<Boolean>
        get() = context.prefDataStore.data.map {
            it[IS_LOGGED_IN] ?: false
        }
        set(value) {
            suspend {
                context.prefDataStore.edit {
                    it[IS_LOGGED_IN] = value.single()
                }
            }
        }

    suspend fun setLogin(boolean: Boolean) {

        context.prefDataStore.edit {
            it[IS_LOGGED_IN] = boolean
        }
    }

    var accessToken: Flow<String>
        get() = context.prefDataStore.data.map {
            it[ACCESS_TOKEN] ?: ""
        }
        set(value) {
            suspend {
                context.prefDataStore.edit {
                    it[ACCESS_TOKEN] = value.single()
                }
            }
        }

    suspend fun setAccessToken(token: String) {
        context.prefDataStore.edit {
            it[ACCESS_TOKEN] = token
        }
    }

    var isFinishOnBoard: Flow<Boolean>
        get() = context.prefDataStore.data.map {
            it[IS_FINISH_ONBOARD] ?: false
        }
        set(value) {
            suspend {
                context.prefDataStore.edit {
                    it[IS_FINISH_ONBOARD] = value.single()
                }
            }
        }

    suspend fun setFinishOnBoard(boolean: Boolean) {
        context.prefDataStore.edit {
            it[IS_FINISH_ONBOARD] = boolean
            logD("setFinishOnBoard")
        }
    }
}