package co.credibanco.transactionstest.transactions.data.remote.model.authorization

data class AuthorizationRequest(
    val id: String,
    val commerceCode: String,
    val terminalCode: String,
    val amount: String,
    val card: String,
)
