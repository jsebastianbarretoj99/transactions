package co.credibanco.transactionstest.transactions.koin

import co.credibanco.transactionstest.database.AppDatabase
import co.credibanco.transactionstest.network.RetrofitProvider
import co.credibanco.transactionstest.providers.DefaultDispatcherProvider
import co.credibanco.transactionstest.providers.DispatcherProvider
import co.credibanco.transactionstest.transactions.data.TransactionsRepository
import co.credibanco.transactionstest.transactions.data.TransactionsRepositoryImpl
import co.credibanco.transactionstest.transactions.data.remote.TransactionsService
import co.credibanco.transactionstest.transactions.ui.transaction_authorization.TransactionAuthorizationViewModel
import co.credibanco.transactionstest.transactions.ui.transaction_detail.TransactionDetailViewModel
import co.credibanco.transactionstest.transactions.ui.transaction_find.TransactionFindViewModel
import co.credibanco.transactionstest.transactions.ui.transaction_list.TransactionListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

val TRANSACTION_MODULE = module {
    // DispatcherProvider instance
    singleOf(::DefaultDispatcherProvider) { bind<DispatcherProvider>() }

    // Retrofit instance
    single {
        RetrofitProvider(
            get(), get()
        ).provideRetrofit()
    }

    // TransactionsService instance
    single {
        get<Retrofit>().create(TransactionsService::class.java)
    }

    // TransactionsDao instance
    single {
        get<AppDatabase>().transactionDao()
    }

    // TransactionsRepository instance
    singleOf(::TransactionsRepositoryImpl) { bind<TransactionsRepository>() }

    // TransactionAuthorizationViewModel instance
    viewModelOf(::TransactionAuthorizationViewModel)

    // TransactionListViewModel instance
    viewModelOf(::TransactionListViewModel)

    // TransactionFindViewModel instance
    viewModelOf(::TransactionFindViewModel)

    // TransactionDetailViewModel instance
    viewModelOf(::TransactionDetailViewModel)

}
