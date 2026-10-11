package com.maxdgf.newspaper.domain.model

import com.maxdgf.newspaper.domain.model.request_parameters.NewsContentCategory

data class TopHeadlinesEndpointSettings(
    val isUsCountry: Boolean = false,
    val category: NewsContentCategory = NewsContentCategory.Default
)
