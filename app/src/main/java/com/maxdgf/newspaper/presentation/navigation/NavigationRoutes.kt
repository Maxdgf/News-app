package com.maxdgf.newspaper.presentation.navigation

import com.maxdgf.newspaper.R

data class ScreenRoute(
    val route: String,
    val associatedIconId: Int,
    val associatedCaption: String
)

val navigationRoutes = listOf(
    ScreenRoute(
        route = "news_articles_view_screen",
        associatedIconId = R.drawable.outline_newsstand_24,
        associatedCaption = "News"
    ),
    ScreenRoute(
        route = "saved_news_articles_view_screen",
        associatedIconId = R.drawable.baseline_save_alt_24,
        associatedCaption = "Saved"
    )
)
