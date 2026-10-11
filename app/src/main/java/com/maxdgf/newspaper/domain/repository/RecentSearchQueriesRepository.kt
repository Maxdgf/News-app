package com.maxdgf.newspaper.domain.repository

import com.maxdgf.newspaper.data.database.news_db.entities.SearchQueryEntity
import kotlinx.coroutines.flow.Flow

interface RecentSearchQueriesRepository {
    fun getAllRecentSearchQueries(): Flow<List<SearchQueryEntity>>

    suspend fun saveSearchQueryToRecents(query: String)

    suspend fun updateLastUseQueryDatetimeById(id: Long)

    suspend fun updateLastUseQueryDatetimeByText(text: String)

    suspend fun deleteSearchQueryByIdFromRecents(id: Long)

    suspend fun deleteAllRecentSearchQueries()
}