package dev.einfantesv.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import dev.einfantesv.presentation.home.PromotionsScreen
import dev.einfantesv.screens.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.einfantesv.UserSessionViewModel
import dev.einfantesv.core.navigation.comprador.BottomNavItem
import dev.einfantesv.presentation.auth.SignUpVendedorScreen
import dev.einfantesv.presentation.home.AdminVendedorScreen
import dev.einfantesv.presentation.home.OrdenesCompradorScreen


//Se agrega para poder usar el BottomBar
@Composable
fun HomeNavigation(mainNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.OrdenesCompradorScreen,
        BottomNavItem.Promotions,
        BottomNavItem.Profile,
    )

    val userSessionViewModel: UserSessionViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomBar(navController = bottomNavController, items = items) }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(mainNavController, userSessionViewModel)
            }
            composable(BottomNavItem.OrdenesCompradorScreen.route) {
                OrdenesCompradorScreen(mainNavController, userSessionViewModel)
            }
            composable(BottomNavItem.Promotions.route) {
                PromotionsScreen(mainNavController)
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(mainNavController, userSessionViewModel)
            }
            composable(BottomNavItem.SignUpVendedor.route) {
                SignUpVendedorScreen(mainNavController)
            }
            composable("adminVendedor") {
                AdminVendedorScreen(mainNavController)
            }

            composable(
                route = "vendedores_que_venden/{nombreProducto}",
                arguments = listOf(navArgument("nombreProducto") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedNombre = backStackEntry.arguments?.getString("nombreProducto") ?: ""
                VendedoresQueVendenScreen(nombreProducto = encodedNombre, navController = mainNavController)
            }


        }
    }
}
