package com.unbelievable.justfacts.kotlinmodule.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unbelievable.justfacts.kotlinmodule.Utils.loadStoriesFromAssets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<MainUIState>(MainUIState.Loading)
    val uiState: StateFlow<MainUIState> = _uiState
    var selectedStory by mutableStateOf<StoryModel?>(null)

    var clickCount by mutableStateOf(0)
        private set

 
    fun onStoryClicked(story: StoryModel): Boolean {
        clickCount++
        selectedStory = story
        return clickCount % 3 == 0   // true → show ad
    }

    init {
        viewModelScope.launch {
            // Observe the repository's Flow
            StoryRepository.allStories.collect { stories ->
                if (stories.isNotEmpty()) {
                    _uiState.value = MainUIState.Success(stories)
                }
            }
        }

    }


}