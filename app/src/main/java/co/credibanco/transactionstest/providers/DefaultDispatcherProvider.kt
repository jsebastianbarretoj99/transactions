package co.credibanco.transactionstest.providers

import kotlinx.coroutines.Dispatchers

class DefaultDispatcherProvider: DispatcherProvider {
    override fun getMain() = Dispatchers.Main
    override fun getIO() = Dispatchers.IO
    override fun getDefault() = Dispatchers.Default
    override fun getUnconfined() = Dispatchers.Unconfined
}
