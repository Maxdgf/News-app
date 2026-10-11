package com.maxdgf.newspaper.data.database.news_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maxdgf.newspaper.data.database.news_db.entities.ArticleEntity
import com.maxdgf.newspaper.data.database.news_db.entities.SearchQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query(value = "SELECT * FROM saved_articles")
    fun getAllSavedArticles(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(articleEntity: ArticleEntity)

    @Query(value = "SELECT * FROM saved_articles WHERE articleId = :id")
    fun getSavedArticleById(id: Long): Flow<ArticleEntity?>

    @Query(value = "DELETE FROM saved_articles WHERE articleId = :id")
    suspend fun deleteSavedArticleById(id: Long)

    @Query(value = "DELETE FROM saved_articles")
    suspend fun deleteAllSavedArticles()


    @Query(value = "SELECT * FROM recent_search_queries ORDER BY last_use_datetime DESC")
    fun getAllRecentSearchQueries(): Flow<List<SearchQueryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveSearchQueryToRecents(searchQueryEntity: SearchQueryEntity)

    @Query(value = "UPDATE recent_search_queries SET last_use_datetime = :datetime WHERE queryId = :id")
    suspend fun updateLastUseQueryDatetimeById(datetime: Long, id: Long)

    @Query(value = "UPDATE recent_search_queries SET last_use_datetime = :datetime WHERE text = :text")
    suspend fun updateLastUseQueryDatetimeByText(datetime: Long, text: String)

    @Query(value = "DELETE FROM recent_search_queries WHERE queryId = :id")
    suspend fun deleteSearchQueryByIdFromRecents(id: Long)

    @Query(value = "DELETE FROM recent_search_queries")
    suspend fun deleteAllRecentSearchQueries()
}