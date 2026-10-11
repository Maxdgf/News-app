package com.maxdgf.newspaper.presentation.all_news_screen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NewsUiViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")
        private set

    var isSearchBarExpanded by mutableStateOf(false)
        private set

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun updateSearchBarState(state: Boolean) {
        isSearchBarExpanded = state
    }
}