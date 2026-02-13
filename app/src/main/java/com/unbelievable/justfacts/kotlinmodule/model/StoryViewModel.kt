package com.unbelievable.justfacts.kotlinmodule.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StoryViewModel(private val languagePreferences: LanguagePreferences) : ViewModel() {

    val selectedLanguage = languagePreferences.selectedLanguage
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            "en"
        )

    fun changeLanguage(lang: String) {
        viewModelScope.launch {
            languagePreferences.saveLanguage(lang)
        }
    }
}