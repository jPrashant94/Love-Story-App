package com.unbelievable.justfacts.kotlinmodule.model

import kotlinx.serialization.Serializable

@Serializable
data class StoryModel(
    val id: Int = 0,
    val title_hn: String = "",
    val title_en: String = "",
    val title_gj: String = "",
    val des_hn: String = "",
    val des_en: String = "",
    val des_gj: String = "",
)

fun StoryModel.getTitle(language: String): String {
    return when (language) {
        "hi" -> title_hn
        "gu" -> title_gj
        else -> title_en
    }
}

fun StoryModel.getDesc(language: String): String {
    return when (language) {
        "hi" -> des_hn
        "gu" -> des_gj
        else -> des_en
    }
}

data class StoriesWrapper(
    val Stories: List<StoryModel> = emptyList()  // ✅ Default value is CRUCIAL
)
