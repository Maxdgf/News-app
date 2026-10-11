package com.maxdgf.newspaper.data.database.news_db.mappers

import com.maxdgf.newspaper.data.database.news_db.entities.ArticleEntity
import com.maxdgf.newspaper.domain.model.Article
import com.maxdgf.newspaper.domain.model.SavedArticle

fun Article.toArticleEntity() = ArticleEntity(
    sourceName = this.source.name,
    sourceId = this.source.id,
    title = this.title,
    author = this.author,
    description = this.description,
    url = this.url,
    urlToImage = this.urlToImage,
    publishedAt = this.publishedAt,
    content = this.content
)

fun ArticleEntity.toSavedArticle() = SavedArticle(
    id = this.articleId,
    title = this.title,
    author = this.author,
    description = this.description,
    url = this.url,
    urlToImage = this.urlToImage,
    publishedAt = this.publishedAt,
    sourceId = this.sourceId,
    sourceName = this.sourceName,
    content = this.content
)
