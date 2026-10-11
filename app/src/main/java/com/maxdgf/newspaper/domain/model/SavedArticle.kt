package com.maxdgf.newspaper.domain.model

data class SavedArticle(
    val id: Long,
    val title: String,
    val author: String?,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceId: String?,
    val sourceName: String,
    val content: String
)