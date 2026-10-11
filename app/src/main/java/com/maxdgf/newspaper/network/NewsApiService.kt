package com.maxdgf.newspaper.network

import com.maxdgf.newspaper.domain.model.request_parameters.NewsContentCategory
import com.maxdgf.newspaper.domain.model.request_parameters.NewsSortMode
import com.maxdgf.newspaper.domain.model.request_parameters.NewsTextLanguage
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

const val CONTENT_PAGE_SIZE = 10

class NewsApiService @Inject constructor(private val _httpClient: HttpClient) {
    private companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY = "" // <- your News API key
    }

    suspend fun getAllArticlesByEverything(
        query: String,
        searchIn: String,
        sortBy: NewsSortMode,
        language: NewsTextLanguage,
        domains: String,
        excludeDomains: String,
        page: Int
    ): HttpResponse {
        // --- Build GET query with 'everything' endpoint ---
        val query = buildString {
            // * add base API's url and target endpoint ('everything')
            appendLine("${BASE_URL}everything?")

            // * add 'q' (query text) request parameter
            append("q=$query")

            // * add 'searchIn' request parameter
            if (searchIn.isNotBlank())
                append("&searchIn=$searchIn")

            // * add 'sortBy' request parameter
            if (sortBy != NewsSortMode.Nothing)
                append("&sort=${sortBy.name.lowercase()}")

            // * add 'language' request parameter
            if (language != NewsTextLanguage.All)
                append("&language=${language.name.lowercase()}")

            // * add 'domains' request parameter
            if (domains.isNotBlank())
                append("&domains=$domains")

            // * add 'excludeDomains' request parameter
            if (excludeDomains.isNotBlank())
                append("&excludeDomains=$excludeDomains")

            // * add 'pageSize' request parameter
            append("&pageSize=$CONTENT_PAGE_SIZE")

            // * add 'page' request parameter
            if (page > 0)
                append("&page=$page")

            // * add 'apiKey' (api key) request parameter
            append("&apiKey=$API_KEY")
        }

        return _httpClient.get(query) // get response
    }

    suspend fun getAllArticlesByTopHeadlines(
        query: String,
        inUsCountry: Boolean,
        category: NewsContentCategory,
        page: Int
    ): HttpResponse {
        // --- Build GET query with 'everything' endpoint ---
        val query = buildString {
            appendLine("${BASE_URL}top-headlines?")

            append("q=$query")

            // * add 'country' request parameter
            // -------------------------------------------------------------
            // [!] According to the News API's documentation
            // for the 'top-headlines' endpoint as of October 2026,
            // the 'country' query parameter can only have one option: 'us'.
            // - link: https://newsapi.org/docs/endpoints/top-headlines
            if (inUsCountry)
                append("&country=us")

            // * add 'category' request parameter
            if (category != NewsContentCategory.Default)
                append("&category=${category.name.lowercase()}")

            // * add 'pageSize' request parameter
            append("&pageSize=$CONTENT_PAGE_SIZE")

            // * add 'page' request parameter
            if (page > 0)
                append("&page=$page")

            // * add 'apiKey' (api key) request parameter
            append("&apiKey=$API_KEY")
        }

        return _httpClient.get(query) // get response
    }
}
