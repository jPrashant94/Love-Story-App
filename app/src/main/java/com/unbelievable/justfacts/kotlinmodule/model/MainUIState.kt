package com.unbelievable.justfacts.kotlinmodule.model

sealed class MainUIState {
    object Loading : MainUIState()
    data class Success(val stories: List<StoryModel>) : MainUIState()
}