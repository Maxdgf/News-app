package com.maxdgf.newspaper.domain.repository.paged


import androidx.paging.PagingSource
import androidx.paging.PagingState

import com.maxdgf.newspaper.domain.model.Article
import com.maxdgf.newspaper.domain.model.ArticleResponseResult
import com.maxdgf.newspaper.domain.model.EverythingEndpointSettings
import com.maxdgf.newspaper.domain.repository.NewsRepository


class NewsByEverythingEndpointSource (
    private val _newsRepository: NewsRepository,
    private val _searchQuery: String,
    private val _everythingEndpointSettings: EverythingEndpointSettings
) : PagingSource<Int, Article>() {
    private var _lastPage = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val nextPage = params.key ?: 1
            _lastPage = nextPage

            val newsList =
                _newsRepository.getAllArticlesByEverything(
                    query = _searchQuery,
                    searchIn = _everythingEndpointSettings.searchIn,
                    sortBy = _everythingEndpointSettings.sortBy,
                    language = _everythingEndpointSettings.language,
                    domains = _everythingEndpointSettings.domains,
                    excludeDomains = _everythingEndpointSettings.excludeDomains,
                    page = nextPage,
                )

            when (newsList) {
                is ArticleResponseResult.Success -> {
                    LoadResult.Page(
                        data = newsList.contentResponse.articles,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = newsList.page + 1
                    )
                }

                is ArticleResponseResult.Error -> {
                    LoadResult.Error(
                        Exception(
                            """
                                status: ${newsList.errorResponse.status}
                                code: ${newsList.errorResponse.code}
                                message: ${newsList.errorResponse.message}
                            """.trimIndent()
                        )
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int = _lastPage
}