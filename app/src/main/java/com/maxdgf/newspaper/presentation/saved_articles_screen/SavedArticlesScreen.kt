package com.maxdgf.newspaper.presentation.saved_articles_screen

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import com.maxdgf.newspaper.R
import com.maxdgf.newspaper.domain.model.SavedArticle
import com.maxdgf.newspaper.presentation.saved_articles_screen.component.RoundedTextButton
import com.maxdgf.newspaper.presentation.saved_articles_screen.component.SavedArticleItem
import com.maxdgf.newspaper.presentation.saved_articles_screen.state.SavedArticlesListResult
import com.maxdgf.newspaper.presentation.saved_articles_screen.viewmodel.SavedArticlesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedArticlesScreen(
    savedNewsArticlesViewModel: SavedArticlesViewModel,
    onShareArticle: (String) -> Boolean,
    onOpenArticle: (String) -> Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val articles by savedNewsArticlesViewModel.allSavedArticles.collectAsState()

        var deleteArticleConfirmationDialogState by rememberSaveable { mutableStateOf(false) }
        var selectedArticleId by remember { mutableStateOf<Long?>(null) }

        if (deleteArticleConfirmationDialogState) {
            AlertDialog(
                onDismissRequest = { deleteArticleConfirmationDialogState = false },
                title = { Text(text = "Delete this article?") },
                text = { Text(text = "Are you sure to delete this saved article?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedArticleId?.let { id ->
                                savedNewsArticlesViewModel.deleteSavedArticle(id)
                                selectedArticleId = null
                            }
                            deleteArticleConfirmationDialogState = false
                        }
                    ) { Text(text = "delete") }
                },
                dismissButton = {
                    TextButton(onClick = { deleteArticleConfirmationDialogState = false }) {
                        Text(text = "cancel")
                    }
                }
            )
        }

        var deleteAllConfirmationDialogState by rememberSaveable { mutableStateOf(false) }
        if (deleteAllConfirmationDialogState) {
            AlertDialog(
                onDismissRequest = { deleteAllConfirmationDialogState = false },
                title = { Text(text = "Delete all articles?") },
                text = { Text(text = "Are you sure to delete all saved articles?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            savedNewsArticlesViewModel.deleteAllSavedArticles()
                            deleteAllConfirmationDialogState = false
                        }
                    ) { Text(text = "delete") }
                },
                dismissButton = {
                    TextButton(onClick = { deleteAllConfirmationDialogState = false }) {
                        Text(text = "cancel")
                    }
                }
            )
        }

        var articleInfoDialogState by remember { mutableStateOf(false) }
        var selectedArticle by remember { mutableStateOf<SavedArticle?>(null) }

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
                            
                            source: ${selectedArticle!!.sourceName}
                            source id: ${selectedArticle!!.sourceId}
                            
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

        when (val articlesState = articles) {
            is SavedArticlesListResult.ContentList -> {
                val context = LocalContext.current

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(10.dp)) }

                    items(
                        items = articlesState.list,
                        key = { article -> article.id }
                    ) { article ->
                        SavedArticleItem(
                            sourceName = article.sourceName,
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
                            onShareArticle = {
                                val message = article.title + "\n\n" + article.url + "\n\n" + article.description
                                if (!onShareArticle(message))
                                    Toast.makeText(context, "Unable to share note!", Toast.LENGTH_SHORT).show()
                            },
                            onDeleteArticle = {
                                deleteArticleConfirmationDialogState = true
                                selectedArticleId = article.id
                            }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(10.dp)) }
                }
            }

            is SavedArticlesListResult.Error -> {
                Text(text = articlesState.message ?: "Unknown error")
            }

            SavedArticlesListResult.EmptyList -> {
                Text(text = "No articles saved yet!")
            }

            SavedArticlesListResult.Loading -> {
                CircularProgressIndicator()
            }
        }

        OutlinedCard(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 5.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { deleteAllConfirmationDialogState = true },
                    shape = CircleShape,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = articles is SavedArticlesListResult.ContentList
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_delete_24),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val allSources by savedNewsArticlesViewModel.allArticleSources.collectAsState()
                val selectedSource by savedNewsArticlesViewModel.selectedSource.collectAsState()

                if (allSources.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        item { Spacer(modifier = Modifier.width(2.dp)) }

                        item {
                            RoundedTextButton(
                                text = "All",
                                isSelected = selectedSource == "all",
                                onClick = { savedNewsArticlesViewModel.selectSource("all") }
                            )
                        }

                        items(items = allSources) {
                            RoundedTextButton(
                                text = it,
                                isSelected = selectedSource == it,
                                onClick = { savedNewsArticlesViewModel.selectSource(it) }
                            )
                        }

                        item { Spacer(modifier = Modifier.width(2.dp)) }
                    }
                } else {
                    Text(text = "Articles sources not found.")
                }
            }
        }
    }
}