package com.coditos.splitmeet.core.network.fcm

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Lightweight DataStore dedicated to storing the FCM token.
 *
 * Separated from [com.coditos.splitmeet.core.storage.TokenDataStore] to avoid
 * coupling the FCM lifecycle with Hilt — [SplitMeetFirebaseMessagingService]
 * does not have access to Hilt-injected dependencies by default.
 */
private val Context.fcmDataStore: DataStore<Preferences> by preferencesDataStore(name = "fcm_prefs")

object FcmTokenDataStore {

    private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")

    suspend fun saveToken(context: Context, token: String) {
        context.fcmDataStore.edit { prefs ->
            prefs[FCM_TOKEN_KEY] = token
        }
    }

    suspend fun getToken(context: Context): String? {
        return context.fcmDataStore.data.map { prefs ->
            prefs[FCM_TOKEN_KEY]
        }.first()
    }
}
