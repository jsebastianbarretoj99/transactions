package co.credibanco.transactionstest.transactions.data.remote.model.authorization

data class AuthorizationResult(
    val receiptId: String,
    val rrn: String,
    val statusCode: String,
    val statusDescription: String,
)
