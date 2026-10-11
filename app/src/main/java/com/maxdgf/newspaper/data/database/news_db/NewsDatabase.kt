package com.maxdgf.newspaper.data.database.news_db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

import com.maxdgf.newspaper.data.database.news_db.entities.ArticleEntity
import com.maxdgf.newspaper.data.database.news_db.entities.SearchQueryEntity


@Database(
    entities = [
        ArticleEntity::class,
        SearchQueryEntity::class
    ],
    version = 5,
    exportSchema = true
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
}


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) =
        connection.execSQL("CREATE TABLE recent_search_queries (queryId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, text TEXT NOT NULL)")
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) =
        connection.execSQL("CREATE UNIQUE INDEX index_recent_search_queries_text ON recent_search_queries (text)")
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(connection: SQLiteConnection) =
        connection.execSQL("ALTER TABLE recent_search_queries ADD COLUMN last_use_datetime INTEGER NOT NULL DEFAULT 0")
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(connection: SQLiteConnection) =
        connection.execSQL("CREATE UNIQUE INDEX index_saved_articles_article ON saved_articles (title, description, url, url_to_image, content)")
}
