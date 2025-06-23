package dev.einfantesv.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.einfantesv.firebase.FirebaseAuthManager

import dev.einfantesv.presentation.auth.LoginScreen
import dev.einfantesv.presentation.auth.ResetPasswordScreen
import dev.einfantesv.presentation.auth.SignUpScreen
import dev.einfantesv.presentation.auth.SignUpScreen2
import dev.einfantesv.presentation.auth.SignUpScreen3
import dev.einfantesv.presentation.auth.SignUpVendedorScreen
import dev.einfantesv.presentation.home.AdminVendedorScreen
import dev.einfantesv.presentation.home.HomeVendedorScreen
import dev.einfantesv.presentation.home.ProductosDelVendedorScreen
import dev.einfantesv.presentation.home.SeleccionarCantidadScreen
import dev.einfantesv.presentation.home.VendedoresQueVendenScreen2
import dev.einfantesv.screens.VendedoresQueVendenScreen


@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    // Verifica si ya hay una sesión activa
    val isLoggedIn = remember { FirebaseAuthManager.isUserLoggedIn() }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login"
    ){
        composable(Screens.Login.route) { LoginScreen(navController) }
        composable(Screens.Home.route) { HomeNavigation(navController) }
        composable(Screens.SignUp.route) { SignUpScreen(navController) }
        composable(Screens.SignUp2.route) { SignUpScreen2(navController) }
        composable(Screens.SignUp3.route) { SignUpScreen3(navController) }
        composable(Screens.ResetPassword.route) { ResetPasswordScreen(navController) }
        composable("signUpVendedor") { SignUpVendedorScreen(navController) }
        composable("adminVendedor") { AdminVendedorScreen(navController) }
        // Asegúrate de tener esta ruta:
        composable("homeVendedor") {
            HomeVendedorScreen(navController)
        }
        composable("productos_vendedor/{vendedorId}") { backStackEntry ->
            val vendedorId = backStackEntry.arguments?.getString("vendedorId") ?: ""
            ProductosDelVendedorScreen(vendedorId = vendedorId, navController = navController)
        }

        composable("seleccionarCantidad/{productoId}") { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
            SeleccionarCantidadScreen(productoId = productoId, navController = navController)
        }

        composable(
            route = "vendedores_que_venden/{nombreProducto}",
            arguments = listOf(navArgument("nombreProducto") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedNombre = backStackEntry.arguments?.getString("nombreProducto") ?: ""
            VendedoresQueVendenScreen(nombreProducto = encodedNombre, navController = navController)
        }


    }
}