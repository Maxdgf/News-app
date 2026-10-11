package com.maxdgf.newspaper.presentation.all_news_screen.state

import com.maxdgf.newspaper.domain.model.SearchQuery

sealed class RecentSearchQueriesListResult {
    data class ContentList(val list: List<SearchQuery>) : RecentSearchQueriesListResult()
    object EmptyList : RecentSearchQueriesListResult()
    data class Error(val message: String?) : RecentSearchQueriesListResult()
    object Loading : RecentSearchQueriesListResult()
}