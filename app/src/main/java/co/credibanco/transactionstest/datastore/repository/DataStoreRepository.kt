package co.credibanco.transactionstest.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.credibanco.transactionstest.transactions.model.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface DataStoreRepository {
    suspend fun readData(key: String): Flow<Response<String>>
    suspend fun writeData(key: String, value: String): Flow<Response<String>>
    suspend fun removeData(key: String): Flow<Response<String>>
}

class DataStoreRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {
    override suspend fun readData(key: String): Flow<Response<String>> {
        return dataStore.data.map { preferences ->
            val preferencesKey = stringPreferencesKey(key)
            if (preferences.contains(preferencesKey)) {
                Response.Success(data = preferences[preferencesKey]?: "")
            } else {
                Response.Failure(error = Throwable(KEY_NOT_FOUND))
            }
        }.catch {
            emit(Response.Failure(error = Throwable(ERROR_NOT_DEFINED)))
        }
    }

    override suspend fun writeData(key: String, value: String): Flow<Response<String>> = flow {
        dataStore.edit { preferences ->
            try {
                preferences[stringPreferencesKey(key)] = value
                emit(Response.Success(data = value))
            } catch (e: Exception) {
                emit(Response.Failure(error = Throwable(ERROR_NOT_DEFINED)))
            }
        }
    }

    override suspend fun removeData(key: String): Flow<Response<String>> = flow {
        dataStore.edit { preferences ->
            try {
                val preferencesKey = stringPreferencesKey(key)
                if (preferences.contains(preferencesKey)) {
                    val data = preferences.remove(preferencesKey)
                    emit(Response.Success(data = data))
                } else {
                    emit(Response.Failure(error = Throwable(KEY_NOT_FOUND)))
                }
            } catch (e: Exception) {
                emit(Response.Failure(error = Throwable(ERROR_NOT_DEFINED)))
            }
        }
    }

    companion object {
        const val KEY_NOT_FOUND = "Key not found"
        const val ERROR_NOT_DEFINED = "Error not defined"
    }
}
