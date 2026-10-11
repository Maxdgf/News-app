package com.maxdgf.newspaper.domain.model.mappers

import com.maxdgf.newspaper.domain.model.TopHeadlinesEndpointSettings
import com.maxdgf.newspaper.domain.model.request_parameters.NewsContentCategory
import com.maxdgf.newspaper.proto.NewsCategory
import com.maxdgf.newspaper.proto.TopHeadlinesEndpointSearchSettings

private fun NewsCategory.toDomainEnum() = when (this) {
    NewsCategory.Default -> NewsContentCategory.Default
    NewsCategory.Business -> NewsContentCategory.Business
    NewsCategory.Entertainment -> NewsContentCategory.Entertainment
    NewsCategory.General -> NewsContentCategory.General
    NewsCategory.Health -> NewsContentCategory.Health
    NewsCategory.Science -> NewsContentCategory.Science
    NewsCategory.Sports -> NewsContentCategory.Sports
    NewsCategory.Technology -> NewsContentCategory.Technology
    NewsCategory.UNRECOGNIZED -> NewsContentCategory.Default
}

fun NewsContentCategory.toProtoEnum() = when (this) {
    NewsContentCategory.Default -> NewsCategory.Default
    NewsContentCategory.Business -> NewsCategory.Business
    NewsContentCategory.Entertainment -> NewsCategory.Entertainment
    NewsContentCategory.General -> NewsCategory.General
    NewsContentCategory.Health -> NewsCategory.Health
    NewsContentCategory.Science -> NewsCategory.Science
    NewsContentCategory.Sports -> NewsCategory.Sports
    NewsContentCategory.Technology -> NewsCategory.Technology
}

fun TopHeadlinesEndpointSearchSettings.toDomainThModel() = TopHeadlinesEndpointSettings(
    isUsCountry = this.isUsCountry,
    category = this.category.toDomainEnum()
)

fun TopHeadlinesEndpointSettings.asProtoModelBuilder() = TopHeadlinesEndpointSearchSettings.newBuilder()
    .setIsUsCountry(this.isUsCountry)
    .setCategory(this.category.toProtoEnum())