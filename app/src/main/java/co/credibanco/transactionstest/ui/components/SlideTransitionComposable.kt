package co.credibanco.transactionstest.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import co.credibanco.transactionstest.ui.components.bottombar.BottomNavItem

fun NavGraphBuilder.slideTransitionComposable(
    bottomNavItem: BottomNavItem,
    arguments: List<NamedNavArgument> = emptyList(),
    durationMillis: Int = 500,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = bottomNavItem.route,
        arguments = arguments,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 0 },
                animationSpec = tween(durationMillis)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis/4)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { 0 },
                animationSpec = tween(durationMillis)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis/4)
            )
        },
        content = content
    )
}
