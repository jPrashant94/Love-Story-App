package com.unbelievable.justfacts.kotlinmodule.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FontSizeModel : ViewModel() {

    private val _uiState = MutableStateFlow(FontUIState())
    val uiState: StateFlow<FontUIState> = _uiState

    fun increaseFont(){
        _uiState.value = _uiState.value.copy(
            fontSize = when (_uiState.value.fontSize) {
                16f -> 18f
                18f -> 22f
                else -> 16f
            }
        )
    }
}