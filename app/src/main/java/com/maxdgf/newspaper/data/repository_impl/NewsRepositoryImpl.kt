package com.maxdgf.newspaper.data.repository_impl

import com.maxdgf.newspaper.domain.model.ArticleResponseResult
import com.maxdgf.newspaper.domain.model.reponse.ContentResponse
import com.maxdgf.newspaper.domain.model.reponse.ErrorResponse
import com.maxdgf.newspaper.domain.model.request_parameters.NewsContentCategory
import com.maxdgf.newspaper.domain.model.request_parameters.NewsSortMode
import com.maxdgf.newspaper.domain.model.request_parameters.NewsTextLanguage
import com.maxdgf.newspaper.domain.repository.NewsRepository
import com.maxdgf.newspaper.network.NewsApiService
import io.ktor.client.call.body
import io.ktor.serialization.JsonConvertException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val _newsApiService: NewsApiService) : NewsRepository {
    override suspend fun getAllArticlesByEverything(
        query: String,
        searchIn: String,
        sortBy: NewsSortMode,
        language: NewsTextLanguage,
        domains: String,
        excludeDomains: String,
        page: Int
    ): ArticleResponseResult {
       // get http response by 'everything' endpoint
       val response = _newsApiService
            .getAllArticlesByEverything(
                query = query,
                searchIn = searchIn,
                sortBy = sortBy,
                language = language,
                domains = domains,
                excludeDomains = excludeDomains,
                page = page
            )

       return try {
           // return content response
           ArticleResponseResult.Success(response.body<ContentResponse>(), page)
       } catch (_: JsonConvertException) {
           // return error response
           ArticleResponseResult.Error(response.body<ErrorResponse>())
       }
    }

    override suspend fun getAllArticlesByTopHeadlines(
        query: String,
        inUsCountry: Boolean,
        category: NewsContentCategory,
        page: Int
    ): ArticleResponseResult {
        // get http response by 'top-headlines' endpoint
        val response = _newsApiService
            .getAllArticlesByTopHeadlines(
                query = query,
                inUsCountry = inUsCountry,
                category = category,
                page = page
            )

        return try {
            // return content response
            ArticleResponseResult.Success(response.body<ContentResponse>(), page)
        } catch (_: JsonConvertException) {
            // return error response
            ArticleResponseResult.Error(response.body<ErrorResponse>())
        }
    }
}