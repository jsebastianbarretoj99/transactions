package co.credibanco.transactionstest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest


@OptIn(ExperimentalCoroutinesApi::class)
open class BaseTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    open fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    open fun tearDown() {
        Dispatchers.resetMain()
    }
}
