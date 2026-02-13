package com.unbelievable.justfacts.kotlinmodule.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings_prefs")
class LanguagePreferences(private val context: Context) {
    companion object {
        // 2. Define a key for your language setting
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }

    // 3. READ: Get the language as a Flow
    // Default to "en" (English) if nothing is saved
    val selectedLanguage: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] ?: "en"
        }

    // 4. WRITE: Save the user's language choice
    suspend fun saveLanguage(langCode: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = langCode
        }
    }
}