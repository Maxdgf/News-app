package com.maxdgf.newspaper.data.repository_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.maxdgf.newspaper.domain.repository.NewsSearchSettingsRepository
import com.maxdgf.newspaper.proto.EverythingEndpointSearchSettings
import com.maxdgf.newspaper.proto.TopHeadlinesEndpointSearchSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsSearchSettingsRepositoryImpl @Inject constructor(private val _dataStore: DataStore<Preferences>) : NewsSearchSettingsRepository {
    companion object {
        val currentEndpoint = intPreferencesKey("current_endpoint")
        val everythingEndpointSearchSettings = byteArrayPreferencesKey("everything_endpoint_search_settings")
        val topHeadlinesEndpointSearchSettings = byteArrayPreferencesKey("top_headlines_endpoint_search_settings")
    }

    override fun getCurrentEndpoint(): Flow<Int> =_dataStore.data.map {
        return@map it[currentEndpoint] ?: 0
    }

    override suspend fun saveCurrentEndpoint(index: Int) {
        _dataStore.edit {
            it[currentEndpoint] = index
        }
    }

    override fun getEverythingEndpointSearchSettings(): Flow<EverythingEndpointSearchSettings> = _dataStore.data.map {
        return@map it[everythingEndpointSearchSettings]?.let { data ->
            EverythingEndpointSearchSettings.parseFrom(data)
        } ?: EverythingEndpointSearchSettings.getDefaultInstance()
    }

    override suspend fun saveEverythingEndpointSearchSettings(settings: EverythingEndpointSearchSettings) {
        _dataStore.edit {
            it[everythingEndpointSearchSettings] = settings.toByteArray()
        }
    }

    override fun getTopHeadlinesEndpointSearchSettings(): Flow<TopHeadlinesEndpointSearchSettings> = _dataStore.data.map {
        return@map it[topHeadlinesEndpointSearchSettings]?.let { data ->
            TopHeadlinesEndpointSearchSettings.parseFrom(data)
        } ?: TopHeadlinesEndpointSearchSettings.getDefaultInstance()
    }

    override suspend fun saveTopHeadlinesEndpointSearchSettings(settings: TopHeadlinesEndpointSearchSettings) {
        _dataStore.edit {
            it[topHeadlinesEndpointSearchSettings] = settings.toByteArray()
        }
    }
}