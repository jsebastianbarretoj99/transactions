package co.credibanco.transactionstest.transactions.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import co.credibanco.transactionstest.ui.components.bottombar.BottomNavItem
import co.credibanco.transactionstest.ui.components.bottombar.BottomNavItem.Companion.tabs
import co.credibanco.transactionstest.ui.components.bottombar.BottomNavigationBar
import co.credibanco.transactionstest.transactions.ui.transaction_annulment.TransactionAnnulmentScreen
import co.credibanco.transactionstest.transactions.ui.transaction_authorization.TransactionAuthorizationScreen
import co.credibanco.transactionstest.transactions.ui.transaction_find.TransactionFindScreen
import co.credibanco.transactionstest.transactions.ui.transaction_list.TransactionListScreen
import co.credibanco.transactionstest.ui.components.TransactionTopBar
import co.credibanco.transactionstest.ui.components.slideTransitionComposable

@Composable
fun HomeScreen(
    transitionToDetail: (String) -> Unit
) {
    val navController = rememberNavController()
    var title by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TransactionTopBar(
                title = title,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                items = tabs,
                navController = navController
            )
        }
    ) { innerPadding ->
        val modifierWithPadding = Modifier.padding(
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding(),
            start = 16.dp,
            end = 16.dp,
        )

        NavHost(navController = navController, startDestination = BottomNavItem.TransactionList.route) {
            slideTransitionComposable(BottomNavItem.TransactionList) {
                title = "Transacciones"
                TransactionListScreen(
                    modifier = modifierWithPadding.testTag("Tab ${BottomNavItem.TransactionList.id}"),
                    transitionToDetail = transitionToDetail,
                )
            }

            slideTransitionComposable(BottomNavItem.TransactionAuthorization) {
                title = "Autorización de transacciones"
                TransactionAuthorizationScreen(
                    modifier = modifierWithPadding.testTag("Tab ${BottomNavItem.TransactionAuthorization.id}")
                )
            }

            slideTransitionComposable(BottomNavItem.TransactionAnnulment) {
                title = "Anulación de transacciones"
                TransactionAnnulmentScreen(
                    modifier = modifierWithPadding.testTag("Tab ${BottomNavItem.TransactionAnnulment.id}")
                )
            }

            slideTransitionComposable(BottomNavItem.TransactionFind) {
                title = "Buscar transacciones"
                TransactionFindScreen(
                    modifier = modifierWithPadding.testTag("Tab ${BottomNavItem.TransactionFind.id}")
                )
            }
        }
    }
}
