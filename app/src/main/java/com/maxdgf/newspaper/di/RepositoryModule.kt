package com.maxdgf.newspaper.di

import com.maxdgf.newspaper.data.repository_impl.NewsRepositoryImpl
import com.maxdgf.newspaper.data.repository_impl.NewsSearchSettingsRepositoryImpl
import com.maxdgf.newspaper.data.repository_impl.RecentSearchQueriesRepositoryImpl
import com.maxdgf.newspaper.data.repository_impl.SavedNewsRepositoryImpl
import com.maxdgf.newspaper.domain.repository.NewsRepository
import com.maxdgf.newspaper.domain.repository.NewsSearchSettingsRepository
import com.maxdgf.newspaper.domain.repository.RecentSearchQueriesRepository
import com.maxdgf.newspaper.domain.repository.SavedNewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    abstract fun bindSavedNewsRepository(impl: SavedNewsRepositoryImpl): SavedNewsRepository

    @Binds
    @Singleton
    abstract fun bindRecentSearchQueriesRepository(impl: RecentSearchQueriesRepositoryImpl): RecentSearchQueriesRepository

    @Binds
    @Singleton
    abstract fun bindNewsSearchSettingsRepository(impl: NewsSearchSettingsRepositoryImpl): NewsSearchSettingsRepository
}