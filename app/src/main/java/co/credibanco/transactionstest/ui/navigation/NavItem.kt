package co.credibanco.transactionstest.ui.navigation

sealed class NavItem(
    val route: String,
) {
    data object Home : NavItem(
        route = "home",
    )

    data object TransactionDetail : NavItem(
        route = "transaction_detail/{receiptId}",
    ) {
        fun createRoute(receiptId: String): String {
            return "transaction_detail/$receiptId"
        }
    }
}
