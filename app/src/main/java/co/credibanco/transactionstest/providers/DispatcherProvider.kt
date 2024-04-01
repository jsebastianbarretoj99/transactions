package co.credibanco.transactionstest.providers

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun getMain(): CoroutineDispatcher
    fun getIO(): CoroutineDispatcher
    fun getDefault(): CoroutineDispatcher
    fun getUnconfined(): CoroutineDispatcher
}
