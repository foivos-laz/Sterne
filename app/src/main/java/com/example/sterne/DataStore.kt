package com.example.sterne

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.myapp.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStore {
    val PHONE_NUMBER_KEY = stringPreferencesKey("phone_number")

    // Save phone number
    suspend fun savePhoneNumber(context: Context, number: String) {
        context.dataStore.edit { prefs ->
            prefs[PHONE_NUMBER_KEY] = number
        }
    }

    // Read phone number as Flow
    fun getPhoneNumber(context: Context): Flow<String> =
        context.dataStore.data
            .map { prefs ->
                prefs[PHONE_NUMBER_KEY] ?: "112" // default number
            }
}

