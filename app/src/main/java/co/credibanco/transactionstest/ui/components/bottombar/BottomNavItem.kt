package co.credibanco.transactionstest.ui.components.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val id: Int,
    val icon: ImageVector,
    val label: String,
) {
    data object TransactionList : BottomNavItem(
        route = "transaction_list",
        id = 0,
        icon = Icons.Default.Home,
        label = "Transacciones"
    )
    data object TransactionAuthorization : BottomNavItem(
        route = "transaction_authorization",
        id = 1,
        icon = Icons.Default.CheckCircle,
        label = "Autorización"
    )
    data object TransactionFind : BottomNavItem(
        route = "transaction_find",
        id = 3,
        icon = Icons.Default.Search,
        label = "Buscar"
    )

    companion object {
        val tabs = listOf(
            TransactionList,
            TransactionAuthorization,
            TransactionFind
        )
    }
}