package com.maxdgf.newspaper.di

import android.content.Context
import androidx.room.Room
import com.maxdgf.newspaper.data.database.news_db.MIGRATION_1_2
import com.maxdgf.newspaper.data.database.news_db.MIGRATION_2_3
import com.maxdgf.newspaper.data.database.news_db.MIGRATION_3_4
import com.maxdgf.newspaper.data.database.news_db.MIGRATION_4_5
import com.maxdgf.newspaper.data.database.news_db.NewsDao
import com.maxdgf.newspaper.data.database.news_db.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(context, NewsDatabase::class.java, "news_database")
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5
            )
            .build()

    @Provides
    fun provideNewsDatabaseDao(newsDatabase: NewsDatabase): NewsDao =
        newsDatabase.getNewsDao()
}