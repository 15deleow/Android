package com.example.flightsearch.data.dataStore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SearchTextRepository(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val SEARCH_TEXT_KEY = stringPreferencesKey("search_text_key")
        const val TAG = "SearchTextRepository"
    }

    val saveSearchText: Flow<String?> = dataStore.data
        .catch {
            if(it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SEARCH_TEXT_KEY]
        }

    suspend fun saveSearchTextPreference(savedSearchText: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_TEXT_KEY] = savedSearchText
        }
    }
}

