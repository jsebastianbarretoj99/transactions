package co.credibanco.transactionstest.datastore.koin

import co.credibanco.transactionstest.datastore.provider.DataStoreProvider
import co.credibanco.transactionstest.datastore.repository.DataStoreRepository
import co.credibanco.transactionstest.datastore.repository.DataStoreRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DATA_STORE_MODULE = module {
    // Datastore instance
    single {
        DataStoreProvider(androidContext()).providePreferencesDataStore()
    }

    // DataStoreRepository instance
    singleOf(::DataStoreRepositoryImpl) { bind<DataStoreRepository>() }
}
