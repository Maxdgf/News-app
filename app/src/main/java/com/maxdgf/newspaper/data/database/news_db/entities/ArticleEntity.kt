package com.maxdgf.newspaper.data.database.news_db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_articles",
    indices = [
        Index(
            value = [
                "title",
                "description",
                "url",
                "url_to_image",
                "content"
            ],
            unique = true
        )
    ]
)
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val articleId: Long = 0L,

    @ColumnInfo(name = "source_name")
    val sourceName: String,

    @ColumnInfo(name = "source_id")
    val sourceId: String?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "url_to_image")
    val urlToImage: String?,

    @ColumnInfo(name = "published_at")
    val publishedAt: String,

    @ColumnInfo(name = "content")
    val content: String
)