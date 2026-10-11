package com.maxdgf.newspaper.presentation.all_news_screen.event

import com.maxdgf.newspaper.domain.model.request_parameters.NewsSortMode
import com.maxdgf.newspaper.domain.model.request_parameters.NewsTextLanguage

sealed interface SaveEverythingEndpointSettingsEvent {
    data class UpdateSearchIn(val data: String) : SaveEverythingEndpointSettingsEvent
    data class UpdateNewsSortMode(val sortMode: NewsSortMode) : SaveEverythingEndpointSettingsEvent
    data class UpdateNewsTextLanguage(val language: NewsTextLanguage) : SaveEverythingEndpointSettingsEvent
    data class IncludeDomains(val domains: String) : SaveEverythingEndpointSettingsEvent
    data class ExcludeDomains(val domains: String) : SaveEverythingEndpointSettingsEvent
}