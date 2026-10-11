package com.maxdgf.newspaper.presentation.all_news_screen

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.maxdgf.newspaper.R
import com.maxdgf.newspaper.domain.model.Article
import com.maxdgf.newspaper.presentation.all_news_screen.component.ArticleItem
import com.maxdgf.newspaper.presentation.all_news_screen.component.PlaceholderArticleItem
import com.maxdgf.newspaper.presentation.all_news_screen.component.SearchSettingsDialog
import com.maxdgf.newspaper.presentation.all_news_screen.state.RecentSearchQueriesListResult
import com.maxdgf.newspaper.presentation.all_news_screen.viewmodel.NewsUiViewModel
import com.maxdgf.newspaper.presentation.all_news_screen.viewmodel.NewsViewModel
import com.maxdgf.newspaper.presentation.common.component.ErrorElement
import com.maxdgf.newspaper.presentation.common.component.ListLoadingElement
import com.maxdgf.newspaper.presentation.common.component.RecentSearchQueryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllNewsScreen(
    newsViewModel: NewsViewModel,
    onShareArticle: (String) -> Boolean,
    onOpenArticle: (String) -> Boolean
) {
    var searchSettingsDialogState by rememberSaveable { mutableStateOf(false) }
    if (searchSettingsDialogState) {
        val currentEndpointIndex by newsViewModel.currentEndpointIndex.collectAsState()
        val everythingEndpointSettings by newsViewModel.everythingEndPointSettings.collectAsState()
        val topHeadlinesEndpointSettings by newsViewModel.topHeadlinesEndpointSettings.collectAsState()

        SearchSettingsDialog(
            onDismissRequest = { searchSettingsDialogState = false },
            currentEndpointIndex = currentEndpointIndex,
            everythingEndpointSettings = everythingEndpointSettings,
            topHeadlinesEndpointSettings = topHeadlinesEndpointSettings,
            onSaveCurrentEndpointIndex = newsViewModel::saveCurrentEndpointIndex,
            onSaveEverythingEndpointSettings = newsViewModel::saveEverythingEndpointSettings,
            onSaveTopHeadlinesEndpointSettings = newsViewModel::saveTopHeadlinesEndpointSettings
        )
    }

    var clearQueriesConfirmationDialogState by rememberSaveable { mutableStateOf(false) }
    if (clearQueriesConfirmationDialogState) {
        AlertDialog(
            onDismissRequest = { clearQueriesConfirmationDialogState = false },
            title = { Text(text = "Clear recent queries") },
            text = { Text(text = "Are you sure to clear all recent queries?") },
            confirmButton = {
                TextButton(onClick = newsViewModel::deleteAllRecentSearchQueries) {
                    Text(text = "clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { clearQueriesConfirmationDialogState = false }) {
                    Text(text = "cancel")
                }
            }
        )
    }

    var deleteQueryConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var selectedQueryIdToDelete by rememberSaveable { mutableStateOf<Long?>(null) }

    if (deleteQueryConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { deleteQueryConfirmationDialog = false },
            title = { Text(text = "Delete recent query") },
            text = { Text(text = "Are you sure to delete this recent query?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedQueryIdToDelete?.let {
                            newsViewModel.deleteSearchQueryByIdFromRecents(it)
                        }
                        deleteQueryConfirmationDialog = false
                    }
                ) { Text(text = "delete") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedQueryIdToDelete = null
                        deleteQueryConfirmationDialog = false
                    }
                ) { Text(text = "cancel") }
            }
        )
    }

    var articleInfoDialogState by remember { mutableStateOf(false) }
    var selectedArticle by remember { mutableStateOf<Article?>(null) }

    if (articleInfoDialogState && selectedArticle != null) {
        val verticalScrollState = rememberScrollState()
        val horizontalScrollState = rememberScrollState()

        AlertDialog(
            onDismissRequest = { articleInfoDialogState = false },
            title = { Text(text = "Article info") },
            text = {
                Text(
                    text =
                        """
                            published at: ${selectedArticle!!.publishedAt}
                            
                            source: ${selectedArticle!!.source.name}
                            source id: ${selectedArticle!!.source.id}
                            
                            title: ${selectedArticle!!.title}
                            description: ${selectedArticle!!.description}
                            
                            author: ${selectedArticle!!.author ?: "Unknown author"}
                            
                            content: ${selectedArticle!!.content}
                        """.trimIndent(),
                    modifier = Modifier
                        .verticalScroll(verticalScrollState)
                        .horizontalScroll(horizontalScrollState)
                )
            },
            confirmButton = {
                TextButton(onClick = { articleInfoDialogState = false }) {
                    Text(text = "Ok")
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val newsUiViewModel: NewsUiViewModel = viewModel()

        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 10.dp,
                    start = 5.dp,
                    end = 5.dp
                )
                .align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    query = newsUiViewModel.searchQuery,
                    onQueryChange = { newValue -> newsUiViewModel.updateSearchQuery(newValue) },
                    onSearch = {
                        if (newsUiViewModel.searchQuery.isBlank())
                            return@InputField

                        newsViewModel.run {
                            searchNewsArticlesByUserQuery(newsUiViewModel.searchQuery)
                            saveSearchQueryToRecents(newsUiViewModel.searchQuery)
                            updateLastUseQueryDatetimeByUserQueryText()
                        }

                        newsUiViewModel.updateSearchBarState(false)
                    },
                    expanded = newsUiViewModel.isSearchBarExpanded,
                    onExpandedChange = { state -> newsUiViewModel.updateSearchBarState(state) },
                    placeholder = { Text(text = "Search news") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.outline_search_24),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Row {
                            IconButton(onClick = { newsUiViewModel.updateSearchQuery("") }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_clear_24),
                                    contentDescription = null
                                )
                            }

                            IconButton(onClick = { searchSettingsDialogState = true }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_settings_24),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            },
            expanded = newsUiViewModel.isSearchBarExpanded,
            onExpandedChange = { state -> newsUiViewModel.updateSearchBarState(state) }
        ) {
            val recentSearchQueries by newsViewModel.recentSearchQueries.collectAsState()

            when (val queriesState = recentSearchQueries) {
                is RecentSearchQueriesListResult.ContentList -> {
                    Column {
                        LazyColumn(modifier = Modifier.weight(1.0f)) {
                            items(
                                items = queriesState.list,
                                key = { query -> query.id }
                            ) { query ->
                                RecentSearchQueryItem(
                                    text = query.text,
                                    onClick = {
                                        newsViewModel.run {
                                            newsUiViewModel.updateSearchQuery(query.text)
                                            searchNewsArticlesByUserQuery(query.text)
                                            updateLastUseQueryDatetimeById(query.id)
                                        }

                                        newsUiViewModel.run {
                                            updateSearchQuery(query.text)
                                            updateSearchBarState(false)
                                        }
                                    },
                                    onLongClick = {
                                        selectedQueryIdToDelete = query.id
                                        deleteQueryConfirmationDialog = true
                                    }
                                )
                            }
                        }

                        TextButton(
                            onClick = { clearQueriesConfirmationDialogState = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) { Text(text = "clear recent queries") }
                    }
                }

                is RecentSearchQueriesListResult.Error -> {
                    Box(modifier = Modifier.weight(1.0f)) {
                        Text(
                            text = queriesState.message ?: "An unexpected error occurred while loading recent queries."
                        )
                    }
                }

                RecentSearchQueriesListResult.Loading -> {
                    Box(modifier = Modifier.weight(1.0f)) { CircularProgressIndicator() }
                }

                RecentSearchQueriesListResult.EmptyList -> {
                    Box(modifier = Modifier.weight(1.0f)) {
                        Text(text = "It looks like you haven't searched for anything recently.")
                    }
                }
            }
        }

        val isSearchedYet by newsViewModel.isSearchedYet.collectAsState()
        if (!isSearchedYet) {
            Text(
                text = "start search news!",
                modifier = Modifier.align(Alignment.Center)
            )
            return
        }

        val context = LocalContext.current
        val foundArticles = newsViewModel.foundNews.collectAsLazyPagingItems()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp),
            userScrollEnabled = foundArticles.loadState.refresh is LoadState.NotLoading,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))
            }

            items(
                count = foundArticles.itemCount,
                key = { index -> index + 1 }
            ) { index ->
                val article = foundArticles[index]

                if (article != null) {
                    ArticleItem(
                        sourceName = article.source.name,
                        title = article.title,
                        urlToImage = article.urlToImage,
                        onClick = {
                            if (!onOpenArticle(article.url))
                                Toast.makeText(context, "Unable to open article!", Toast.LENGTH_SHORT).show()
                        },
                        onGetInfo = {
                            selectedArticle = article
                            articleInfoDialogState = true
                        },
                        onSaveArticle = { newsViewModel.saveArticle(article) },
                        onShareArticle = {
                            val message = article.title + "\n\n" + article.url + "\n\n" + article.description
                            if (!onShareArticle(message))
                                Toast.makeText(context, "Unable to share note!", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else PlaceholderArticleItem()
            }

            foundArticles.run {
                when (loadState.append) {
                    LoadState.Loading -> {
                        item { ListLoadingElement() }
                    }

                    is LoadState.Error -> {
                        val exception = foundArticles.loadState.append as LoadState.Error
                        item {
                            ErrorElement(
                                message = exception.error.message,
                                buttonCaption = "retry",
                                onRetry = { retry() }
                            )
                        }
                    }

                    is LoadState.NotLoading -> {}
                }

                when (loadState.refresh) {
                    is LoadState.Error -> {
                        val exception = foundArticles.loadState.refresh as LoadState.Error
                        item {
                            ErrorElement(
                                message = exception.error.message,
                                buttonCaption = "refresh",
                                onRetry = { refresh() }
                            )
                        }
                    }

                    is LoadState.Loading -> {
                        item { ListLoadingElement() }
                    }

                    is LoadState.NotLoading -> {}
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
