package com.unbelievable.justfacts.kotlinmodule.model

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "theme_prefs")
class ThemePreferences(private val context: Context) {

    companion object {
        private val DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map {
        it[DARK_MODE] ?: false
    }

    suspend fun saveDarkMode(value: Boolean) {
        context.dataStore.edit {
            it[DARK_MODE] = value
        }
    }
}