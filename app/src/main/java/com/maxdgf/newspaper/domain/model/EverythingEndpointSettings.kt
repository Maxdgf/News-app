package com.maxdgf.newspaper.domain.model

import com.maxdgf.newspaper.domain.model.request_parameters.NewsSortMode
import com.maxdgf.newspaper.domain.model.request_parameters.NewsTextLanguage

data class EverythingEndpointSettings(
    val searchIn: String = "",
    val sortBy: NewsSortMode = NewsSortMode.Nothing,
    val language: NewsTextLanguage = NewsTextLanguage.All,
    val domains: String = "",
    val excludeDomains: String = ""
)