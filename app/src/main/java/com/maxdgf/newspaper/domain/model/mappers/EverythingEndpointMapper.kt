package com.maxdgf.newspaper.domain.model.mappers

import com.maxdgf.newspaper.domain.model.EverythingEndpointSettings
import com.maxdgf.newspaper.domain.model.request_parameters.NewsSortMode
import com.maxdgf.newspaper.domain.model.request_parameters.NewsTextLanguage
import com.maxdgf.newspaper.proto.EverythingEndpointSearchSettings
import com.maxdgf.newspaper.proto.NewsLanguage
import com.maxdgf.newspaper.proto.NewsSort

private fun NewsSort.toDomainEnum() = when (this) {
    NewsSort.Nothing -> NewsSortMode.Nothing
    NewsSort.Relevancy -> NewsSortMode.Relevancy
    NewsSort.Popularity -> NewsSortMode.Popularity
    NewsSort.PublishedAt -> NewsSortMode.PublishedAt
    NewsSort.UNRECOGNIZED -> NewsSortMode.Nothing
}

fun NewsSortMode.toProtoSortEnum() = when (this) {
    NewsSortMode.Nothing -> NewsSort.Nothing
    NewsSortMode.Relevancy -> NewsSort.Relevancy
    NewsSortMode.Popularity -> NewsSort.Popularity
    NewsSortMode.PublishedAt -> NewsSort.PublishedAt
}

private fun NewsLanguage.toDomainEnum() = when (this) {
    NewsLanguage.All -> NewsTextLanguage.All
    NewsLanguage.Ar -> NewsTextLanguage.Ar
    NewsLanguage.De -> NewsTextLanguage.De
    NewsLanguage.En -> NewsTextLanguage.En
    NewsLanguage.Es -> NewsTextLanguage.Es
    NewsLanguage.Fr -> NewsTextLanguage.Fr
    NewsLanguage.He -> NewsTextLanguage.He
    NewsLanguage.It -> NewsTextLanguage.It
    NewsLanguage.Nl -> NewsTextLanguage.Nl
    NewsLanguage.No -> NewsTextLanguage.No
    NewsLanguage.Pt -> NewsTextLanguage.Pt
    NewsLanguage.Ru -> NewsTextLanguage.Ru
    NewsLanguage.Sv -> NewsTextLanguage.Sv
    NewsLanguage.Ud -> NewsTextLanguage.Ud
    NewsLanguage.Zh -> NewsTextLanguage.Zh
    NewsLanguage.UNRECOGNIZED -> NewsTextLanguage.All
}

fun NewsTextLanguage.toProtoLanguageEnum() = when (this) {
    NewsTextLanguage.All -> NewsLanguage.All
    NewsTextLanguage.Ar -> NewsLanguage.Ar
    NewsTextLanguage.De -> NewsLanguage.De
    NewsTextLanguage.En -> NewsLanguage.En
    NewsTextLanguage.Es -> NewsLanguage.Es
    NewsTextLanguage.Fr -> NewsLanguage.Fr
    NewsTextLanguage.He -> NewsLanguage.He
    NewsTextLanguage.It -> NewsLanguage.It
    NewsTextLanguage.Nl -> NewsLanguage.Nl
    NewsTextLanguage.No -> NewsLanguage.No
    NewsTextLanguage.Pt -> NewsLanguage.Pt
    NewsTextLanguage.Ru -> NewsLanguage.Ru
    NewsTextLanguage.Sv -> NewsLanguage.Sv
    NewsTextLanguage.Ud -> NewsLanguage.Ud
    NewsTextLanguage.Zh -> NewsLanguage.Zh
}

fun EverythingEndpointSearchSettings.toDomainEModel() = EverythingEndpointSettings(
    searchIn = this.searchIn,
    sortBy = this.sortBy.toDomainEnum(),
    language = this.language.toDomainEnum(),
    domains = this.includeDomains,
    excludeDomains = this.excludeDomains
)

fun EverythingEndpointSettings.asProtoModelBuilder() = EverythingEndpointSearchSettings.newBuilder()
    .setSearchIn(this.searchIn)
    .setSortBy(this.sortBy.toProtoSortEnum())
    .setLanguage(this.language.toProtoLanguageEnum())
    .setIncludeDomains(this.domains)
    .setExcludeDomains(this.excludeDomains)