package com.maxdgf.newspaper.data.database.news_db.mappers

import com.maxdgf.newspaper.data.database.news_db.entities.SearchQueryEntity
import com.maxdgf.newspaper.domain.model.SearchQuery

fun SearchQueryEntity.toSearchQuery() = SearchQuery(
    id = this.queryId,
    text = this.text
)
