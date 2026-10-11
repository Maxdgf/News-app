package com.maxdgf.newspaper.domain.repository

import com.maxdgf.newspaper.proto.EverythingEndpointSearchSettings
import com.maxdgf.newspaper.proto.TopHeadlinesEndpointSearchSettings
import kotlinx.coroutines.flow.Flow

interface NewsSearchSettingsRepository {
    fun getCurrentEndpoint(): Flow<Int>
    suspend fun saveCurrentEndpoint(index: Int)

    fun getEverythingEndpointSearchSettings(): Flow<EverythingEndpointSearchSettings>

    suspend fun saveEverythingEndpointSearchSettings(settings: EverythingEndpointSearchSettings)

    fun getTopHeadlinesEndpointSearchSettings(): Flow<TopHeadlinesEndpointSearchSettings>

    suspend fun saveTopHeadlinesEndpointSearchSettings(settings: TopHeadlinesEndpointSearchSettings)
}