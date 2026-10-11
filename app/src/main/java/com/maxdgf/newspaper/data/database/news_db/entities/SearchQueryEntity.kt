package com.maxdgf.newspaper.data.database.news_db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recent_search_queries",
    indices = [Index(value = ["text"], unique = true)]
)
data class SearchQueryEntity(
    @PrimaryKey(autoGenerate = true)
    val queryId: Long = 0,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "last_use_datetime", defaultValue = "0")
    val lastUseDatetime: Long = System.currentTimeMillis()
)