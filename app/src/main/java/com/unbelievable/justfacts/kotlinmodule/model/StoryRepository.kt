package com.unbelievable.justfacts.kotlinmodule.model

import android.content.Context
import com.unbelievable.justfacts.kotlinmodule.Utils.loadStoriesFromAssets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object StoryRepository {
    private val _allStories = MutableStateFlow<List<StoryModel>>(emptyList())

    // Public state that ViewModels can observe
    val allStories: StateFlow<List<StoryModel>> = _allStories.asStateFlow()



    // Function to load data once
    suspend fun initialize(context: Context) {
        if (_allStories.value.isEmpty()) {
            val stories = loadStoriesFromAssets(context)
            _allStories.value = stories
        }
    }


    fun getStoryById(id: Int): StoryModel? {
        return _allStories.value.find { it.id == id }
    }

    fun getStoryTitle(lang: String, story: StoryModel) : String{
        when(lang){
            "en" -> return story.title_en
            "hn" -> return story.title_hn
            "gj" -> return story.title_gj
        }
        return "English"
    }




}