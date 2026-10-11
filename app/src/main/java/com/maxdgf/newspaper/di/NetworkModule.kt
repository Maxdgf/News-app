package com.maxdgf.newspaper.di

import com.maxdgf.newspaper.network.NewsApiService
import com.maxdgf.newspaper.network.httpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = httpClient

    @Provides
    @Singleton
    fun provideApiService(httpClient: HttpClient): NewsApiService =
        NewsApiService(httpClient)
}