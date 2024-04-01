package co.credibanco.transactionstest.datastore.provider

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

class DataStoreProvider(
    private val appContext: Context,
) {
    fun providePreferencesDataStore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile(USER_PREFERENCES_NAME)
            }
        )
    }

    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
    }
}
