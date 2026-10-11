package com.maxdgf.newspaper.presentation.saved_articles_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxdgf.newspaper.data.database.news_db.mappers.toSavedArticle
import com.maxdgf.newspaper.domain.repository.SavedNewsRepository
import com.maxdgf.newspaper.presentation.saved_articles_screen.state.SavedArticlesListResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedArticlesViewModel @Inject constructor(private val _savedNewsRepository: SavedNewsRepository) : ViewModel() {
    private val _selectedSource = MutableStateFlow("all")
    val selectedSource = _selectedSource.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val allSavedArticles = _savedNewsRepository.getAllSavedArticles()
        .combine(_selectedSource) { articles, source ->
            return@combine Pair(articles, source)
        }
        .flatMapLatest { data ->
            flow {
                if (data.first.isNotEmpty()) {
                    val articles = withContext(Dispatchers.Default) {
                        return@withContext data.first
                            .filter {
                                if (data.second != "all")
                                    it.sourceName == data.second
                                else
                                    return@filter true
                            }
                            .map { it.toSavedArticle() }
                    }
                    emit(SavedArticlesListResult.ContentList(articles))
                } else {
                    emit(SavedArticlesListResult.EmptyList)
                }
            }
            .onStart {
                emit(SavedArticlesListResult.Loading)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SavedArticlesListResult.Loading
        )

    fun selectSource(name: String) {
        _selectedSource.value = name
    }

    fun deleteSavedArticle(id: Long) = viewModelScope.launch {
        _savedNewsRepository.deleteSavedArticleById(id)
    }

    fun deleteAllSavedArticles() = viewModelScope.launch {
        _savedNewsRepository.deleteAllSavedArticles()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val allArticleSources = _savedNewsRepository.getAllSavedArticles()
        .mapLatest { data ->
            return@mapLatest withContext(Dispatchers.Default) {
                return@withContext data
                    .map { it.sourceName }
                    .distinct()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}