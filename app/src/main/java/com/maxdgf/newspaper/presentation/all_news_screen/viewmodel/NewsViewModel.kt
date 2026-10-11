package com.maxdgf.newspaper.presentation.all_news_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.maxdgf.newspaper.data.database.news_db.mappers.toArticleEntity
import com.maxdgf.newspaper.data.database.news_db.mappers.toSearchQuery
import com.maxdgf.newspaper.domain.model.Article
import com.maxdgf.newspaper.domain.model.EverythingEndpointSettings
import com.maxdgf.newspaper.domain.model.TopHeadlinesEndpointSettings
import com.maxdgf.newspaper.domain.model.mappers.asProtoModelBuilder
import com.maxdgf.newspaper.domain.model.mappers.toDomainEModel
import com.maxdgf.newspaper.domain.model.mappers.toDomainThModel
import com.maxdgf.newspaper.domain.model.mappers.toProtoEnum
import com.maxdgf.newspaper.domain.model.mappers.toProtoLanguageEnum
import com.maxdgf.newspaper.domain.model.mappers.toProtoSortEnum
import com.maxdgf.newspaper.domain.repository.NewsRepository
import com.maxdgf.newspaper.domain.repository.NewsSearchSettingsRepository
import com.maxdgf.newspaper.domain.repository.RecentSearchQueriesRepository
import com.maxdgf.newspaper.domain.repository.SavedNewsRepository
import com.maxdgf.newspaper.domain.repository.paged.NewsByEverythingEndpointSource
import com.maxdgf.newspaper.domain.repository.paged.NewsByTopHeadlinesEndpointSource
import com.maxdgf.newspaper.network.CONTENT_PAGE_SIZE
import com.maxdgf.newspaper.presentation.all_news_screen.event.SaveEverythingEndpointSettingsEvent
import com.maxdgf.newspaper.presentation.all_news_screen.event.SaveTopHeadlinesEndpointSettingsEvent
import com.maxdgf.newspaper.presentation.all_news_screen.state.RecentSearchQueriesListResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private data class SearchData(
    val query: String,
    val endpointIndex: Int,
    val everythingEndpointIndex: EverythingEndpointSettings,
    val topHeadlinesEndpointSettings: TopHeadlinesEndpointSettings
)

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val _newsRepository: NewsRepository,
    private val _savedNewsRepository: SavedNewsRepository,
    private val _recentSearchQueriesRepository: RecentSearchQueriesRepository,
    private val _newsSearchSettingsRepository: NewsSearchSettingsRepository
) : ViewModel() {
    val currentEndpointIndex = _newsSearchSettingsRepository.getCurrentEndpoint()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = 0
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val everythingEndPointSettings = _newsSearchSettingsRepository.getEverythingEndpointSearchSettings()
        .mapLatest { data -> return@mapLatest data.toDomainEModel() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = EverythingEndpointSettings()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val topHeadlinesEndpointSettings = _newsSearchSettingsRepository.getTopHeadlinesEndpointSearchSettings()
        .mapLatest { data -> return@mapLatest data.toDomainThModel() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = TopHeadlinesEndpointSettings()
        )

    fun saveCurrentEndpointIndex(index: Int) = viewModelScope.launch(Dispatchers.IO) {
        _newsSearchSettingsRepository.saveCurrentEndpoint(index)
    }

    fun saveEverythingEndpointSettings(event: SaveEverythingEndpointSettingsEvent) {
        val everythingEndpointSettings = everythingEndPointSettings.value.asProtoModelBuilder()

        when (event) {
            is SaveEverythingEndpointSettingsEvent.ExcludeDomains -> everythingEndpointSettings.setExcludeDomains(event.domains)
            is SaveEverythingEndpointSettingsEvent.IncludeDomains -> everythingEndpointSettings.setIncludeDomains(event.domains)
            is SaveEverythingEndpointSettingsEvent.UpdateNewsSortMode -> everythingEndpointSettings.setSortBy(event.sortMode.toProtoSortEnum())
            is SaveEverythingEndpointSettingsEvent.UpdateNewsTextLanguage -> everythingEndpointSettings.setLanguage(event.language.toProtoLanguageEnum())
            is SaveEverythingEndpointSettingsEvent.UpdateSearchIn -> everythingEndpointSettings.setSearchIn(event.data)
        }

        viewModelScope.launch(Dispatchers.IO) {
            _newsSearchSettingsRepository.saveEverythingEndpointSearchSettings(everythingEndpointSettings.build()) // save settings
        }
    }

    fun saveTopHeadlinesEndpointSettings(event: SaveTopHeadlinesEndpointSettingsEvent) {
        val topHeadlinesEndpointSettings = topHeadlinesEndpointSettings.value.asProtoModelBuilder()

        when (event) {
            is SaveTopHeadlinesEndpointSettingsEvent.UpdateIsUsCountryState -> topHeadlinesEndpointSettings.setIsUsCountry(event.state)
            is SaveTopHeadlinesEndpointSettingsEvent.UpdateNewsCategory -> topHeadlinesEndpointSettings.setCategory(event.category.toProtoEnum())
        }

        viewModelScope.launch(Dispatchers.IO) {
            _newsSearchSettingsRepository.saveTopHeadlinesEndpointSearchSettings(topHeadlinesEndpointSettings.build())
        }
    }


    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val foundNews =
        combine(
            _searchQuery,
            _newsSearchSettingsRepository.getCurrentEndpoint(),
            _newsSearchSettingsRepository.getEverythingEndpointSearchSettings(),
            _newsSearchSettingsRepository.getTopHeadlinesEndpointSearchSettings()
        ) { query, endpointIndex, everythingSettings, topHeadlinesSettings ->
            return@combine SearchData(
                query = query,
                endpointIndex = endpointIndex,
                everythingEndpointIndex = everythingSettings.toDomainEModel(),
                topHeadlinesEndpointSettings = topHeadlinesSettings.toDomainThModel()
            )
        }
        .filterNot { data -> data.query.isBlank() }
        .distinctUntilChangedBy { data -> data.query }
        .flatMapLatest { data ->
            return@flatMapLatest Pager(PagingConfig(pageSize = CONTENT_PAGE_SIZE)) {
                when (data.endpointIndex) {
                    0 -> {
                        NewsByEverythingEndpointSource(
                            _newsRepository,
                            data.query,
                            data.everythingEndpointIndex
                        )
                    }

                    1 -> {
                        NewsByTopHeadlinesEndpointSource(
                            _newsRepository,
                            data.query,
                            data.topHeadlinesEndpointSettings
                        )
                    }

                    else -> {
                        NewsByEverythingEndpointSource(
                            _newsRepository,
                            data.query,
                            data.everythingEndpointIndex
                        )
                    }
                }

            }.flow
        }
        .cachedIn(viewModelScope)

    fun searchNewsArticlesByUserQuery(query: String) {
        if (!_isSearchedYet.value) _isSearchedYet.value = true
        _searchQuery.value = query
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        _savedNewsRepository.saveArticle(article.toArticleEntity())
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val recentSearchQueries = _recentSearchQueriesRepository.getAllRecentSearchQueries()
        .mapLatest { recentSearchQueries ->
            return@mapLatest if (recentSearchQueries.isNotEmpty()) {
                val allRecentQueries = withContext(Dispatchers.Default) {
                    return@withContext recentSearchQueries.map { it.toSearchQuery() }
                }
                RecentSearchQueriesListResult.ContentList(allRecentQueries)
            } else {
                RecentSearchQueriesListResult.EmptyList
            }
        }
        .catch { exception ->
            emit(RecentSearchQueriesListResult.Error(exception.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RecentSearchQueriesListResult.Loading
        )

    fun saveSearchQueryToRecents(query: String) = viewModelScope.launch {
        _recentSearchQueriesRepository.saveSearchQueryToRecents(query)
    }

    fun updateLastUseQueryDatetimeById(id: Long) = viewModelScope.launch {
        _recentSearchQueriesRepository.updateLastUseQueryDatetimeById(id)
    }

    fun updateLastUseQueryDatetimeByUserQueryText() = viewModelScope.launch {
        _recentSearchQueriesRepository.updateLastUseQueryDatetimeByText(_searchQuery.value)
    }

    fun deleteSearchQueryByIdFromRecents(id: Long) = viewModelScope.launch {
        _recentSearchQueriesRepository.deleteSearchQueryByIdFromRecents(id)
    }

    fun deleteAllRecentSearchQueries() = viewModelScope.launch {
        _recentSearchQueriesRepository.deleteAllRecentSearchQueries()
    }


    private val _isSearchedYet = MutableStateFlow(false)
    val isSearchedYet = _isSearchedYet.asStateFlow()
}