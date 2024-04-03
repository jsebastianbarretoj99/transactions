package co.credibanco.transactionstest.providers

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestDispatcherProvider: DispatcherProvider {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    override fun getMain() = testDispatcher
    override fun getIO() = testDispatcher
    override fun getDefault() = testDispatcher
    override fun getUnconfined() = testDispatcher
}
