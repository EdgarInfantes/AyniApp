package dev.einfantesv.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import dev.einfantesv.PromotionsScreen
import dev.einfantesv.screens.*

    //Se agrega para poder usar el BottomBar
@Composable
fun HomeNavigation(mainNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Promotions,
        BottomNavItem.Profile,
    )

    Scaffold(
        bottomBar = { BottomBar(navController = bottomNavController, items = items) }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(mainNavController) }
            composable(BottomNavItem.Promotions.route) { PromotionsScreen(mainNavController) }
            composable(BottomNavItem.Profile.route) { ProfileScreen(mainNavController) }
        }
    }
}
