package com.maxdgf.newspaper.presentation.all_news_screen.event

import com.maxdgf.newspaper.domain.model.request_parameters.NewsContentCategory

sealed interface SaveTopHeadlinesEndpointSettingsEvent {
    data class UpdateIsUsCountryState(val state: Boolean) : SaveTopHeadlinesEndpointSettingsEvent
    data class UpdateNewsCategory(val category: NewsContentCategory) : SaveTopHeadlinesEndpointSettingsEvent
}
