package com.maxdgf.newspaper.data.repository_impl

import com.maxdgf.newspaper.data.database.news_db.NewsDao
import com.maxdgf.newspaper.data.database.news_db.entities.ArticleEntity
import com.maxdgf.newspaper.domain.repository.SavedNewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedNewsRepositoryImpl @Inject constructor(private val _newsDao: NewsDao) : SavedNewsRepository {
    override fun getAllSavedArticles(): Flow<List<ArticleEntity>> = _newsDao.getAllSavedArticles()

    override suspend fun saveArticle(articleEntity: ArticleEntity) = _newsDao.saveArticle(articleEntity)

    override fun getSavedArticleById(id: Long): Flow<ArticleEntity?> = _newsDao.getSavedArticleById(id)

    override suspend fun deleteSavedArticleById(id: Long) = _newsDao.deleteSavedArticleById(id)

    override suspend fun deleteAllSavedArticles() = _newsDao.deleteAllSavedArticles()
}