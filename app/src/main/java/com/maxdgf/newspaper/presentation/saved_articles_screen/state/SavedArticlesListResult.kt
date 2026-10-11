package com.maxdgf.newspaper.presentation.saved_articles_screen.state

import com.maxdgf.newspaper.domain.model.SavedArticle

sealed class SavedArticlesListResult {
    data class ContentList(val list: List<SavedArticle>) : SavedArticlesListResult()
    object EmptyList : SavedArticlesListResult()
    data class Error(val message: String?) : SavedArticlesListResult()
    object Loading : SavedArticlesListResult()
}