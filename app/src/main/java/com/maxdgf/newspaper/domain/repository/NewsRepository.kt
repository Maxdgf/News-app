package com.maxdgf.newspaper.domain.repository

import com.maxdgf.newspaper.domain.model.ArticleResponseResult
import com.maxdgf.newspaper.domain.model.request_parameters.NewsContentCategory
import com.maxdgf.newspaper.domain.model.request_parameters.NewsTextLanguage
import com.maxdgf.newspaper.domain.model.request_parameters.NewsSortMode

interface NewsRepository {
    suspend fun getAllArticlesByEverything(
        query: String,
        searchIn: String,
        sortBy: NewsSortMode,
        language: NewsTextLanguage,
        domains: String,
        excludeDomains: String,
        page: Int
    ): ArticleResponseResult

    suspend fun getAllArticlesByTopHeadlines(
        query: String,
        inUsCountry: Boolean,
        category: NewsContentCategory,
        page: Int,
    ): ArticleResponseResult
}