package co.credibanco.transactionstest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.credibanco.transactionstest.transactions.ui.HomeScreen
import co.credibanco.transactionstest.transactions.ui.transaction_detail.TransactionDetailScreen
import co.credibanco.transactionstest.ui.navigation.NavItem
import co.credibanco.transactionstest.ui.theme.TransactionsTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            TransactionsTestTheme {
                NavHost(
                    navController = navController, startDestination = NavItem.Home.route
                ) {
                    composable(NavItem.Home.route) {
                        HomeScreen(
                            transitionToDetail = { receiptId ->
                                navController.navigate(
                                    route = NavItem.TransactionDetail.createRoute(receiptId),
                                )
                            }
                        )
                    }
                    composable(
                        route = NavItem.TransactionDetail.route,
                        arguments = listOf(
                            navArgument("receiptId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        TransactionDetailScreen(
                            transitionToHome = {
                                navController.popBackStack()
                            },
                            receiptId = backStackEntry.arguments?.getString("receiptId")
                        )
                    }
                }
            }
        }
    }
}
