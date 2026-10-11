package com.maxdgf.newspaper.data.repository_impl

import com.maxdgf.newspaper.data.database.news_db.NewsDao
import com.maxdgf.newspaper.data.database.news_db.entities.SearchQueryEntity
import com.maxdgf.newspaper.domain.repository.RecentSearchQueriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentSearchQueriesRepositoryImpl @Inject constructor(private val _newsDao: NewsDao) : RecentSearchQueriesRepository {
    override fun getAllRecentSearchQueries(): Flow<List<SearchQueryEntity>> = _newsDao.getAllRecentSearchQueries()

    override suspend fun saveSearchQueryToRecents(query: String) = _newsDao.saveSearchQueryToRecents(SearchQueryEntity(text = query))

    override suspend fun updateLastUseQueryDatetimeById(id: Long) = _newsDao.updateLastUseQueryDatetimeById(System.currentTimeMillis(), id)

    override suspend fun updateLastUseQueryDatetimeByText(text: String) = _newsDao.updateLastUseQueryDatetimeByText(System.currentTimeMillis(), text)

    override suspend fun deleteSearchQueryByIdFromRecents(id: Long) = _newsDao.deleteSearchQueryByIdFromRecents(id)

    override suspend fun deleteAllRecentSearchQueries() = _newsDao.deleteAllSavedArticles()
}