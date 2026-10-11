package com.maxdgf.newspaper.domain.repository

import com.maxdgf.newspaper.data.database.news_db.entities.ArticleEntity
import kotlinx.coroutines.flow.Flow

interface SavedNewsRepository {
    fun getAllSavedArticles(): Flow<List<ArticleEntity>>

    suspend fun saveArticle(articleEntity: ArticleEntity)

    fun getSavedArticleById(id: Long): Flow<ArticleEntity?>

    suspend fun deleteSavedArticleById(id: Long)

    suspend fun deleteAllSavedArticles()
}