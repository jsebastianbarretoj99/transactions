package co.credibanco.transactionstest.transactions.model

sealed class Response<T>(
    val data: T? = null,
    val error: Throwable? = null,
) {
    class Success<T>(data: T) : Response<T>(data)
    class Failure<T>(error: Throwable?) : Response<T>(null, error)
}
