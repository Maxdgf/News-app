package com.maxdgf.newspaper.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maxdgf.newspaper.presentation.all_news_screen.AllNewsScreen
import com.maxdgf.newspaper.presentation.all_news_screen.viewmodel.NewsViewModel
import com.maxdgf.newspaper.presentation.common.utils.ShareManager
import com.maxdgf.newspaper.presentation.common.utils.UrlOpener
import com.maxdgf.newspaper.presentation.navigation.navigationRoutes
import com.maxdgf.newspaper.presentation.saved_articles_screen.SavedArticlesScreen
import com.maxdgf.newspaper.presentation.saved_articles_screen.viewmodel.SavedArticlesViewModel

@Composable
fun NewspaperNavGraphRoot() {
    val navController = rememberNavController()
    var selectedDestination by rememberSaveable { mutableStateOf(navigationRoutes[0].route) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                navigationRoutes.forEach { destination ->
                    NavigationBarItem(
                        selected = selectedDestination == destination.route,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = destination.route
                        },
                        icon = {
                            Icon(
                                painter = painterResource(destination.associatedIconId),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = destination.associatedCaption) }
                    )
                }
            }
        }
    ) { contentPadding ->
        val newsViewModel: NewsViewModel = hiltViewModel()
        val savedNewsArticlesViewModel: SavedArticlesViewModel = hiltViewModel()

        val context = LocalContext.current

        val shareManager = remember { ShareManager(context) }
        val urlOpener = remember { UrlOpener(context) }

        NavHost(
            navController = navController,
            startDestination = navigationRoutes[0].route,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(route = navigationRoutes[0].route) {
                AllNewsScreen(
                    newsViewModel = newsViewModel,
                    onShareArticle = shareManager::shareMessage,
                    onOpenArticle = urlOpener::openUrlInBrowser
                )
            }

            composable(route = navigationRoutes[1].route) {
                SavedArticlesScreen(
                    savedNewsArticlesViewModel = savedNewsArticlesViewModel,
                    onShareArticle = shareManager::shareMessage,
                    onOpenArticle = urlOpener::openUrlInBrowser
                )
            }
        }
    }
}