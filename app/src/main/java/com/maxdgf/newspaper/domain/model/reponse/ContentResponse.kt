package com.maxdgf.newspaper.domain.model.reponse

import com.maxdgf.newspaper.domain.model.Article
import kotlinx.serialization.Serializable

@Serializable
data class ContentResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)