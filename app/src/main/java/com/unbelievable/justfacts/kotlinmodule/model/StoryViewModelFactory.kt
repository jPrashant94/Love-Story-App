package com.unbelievable.justfacts.kotlinmodule.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StoryViewModelFactory(private val languagePreferences: LanguagePreferences) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(languagePreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}