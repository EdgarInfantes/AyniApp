package dev.einfantesv.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.einfantesv.HomeScreen
import dev.einfantesv.LoginScreen
import dev.einfantesv.ResetPasswordScreen
import dev.einfantesv.SignUpScreen


@Composable
fun NavigationWrapper(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Login.route) {
        composable(Screens.Login.route) { LoginScreen(navController) }
        composable(Screens.Home.route) { HomeScreen(navController) }
        composable(Screens.SignUp.route) { SignUpScreen(navController) }
        composable(Screens.ResetPassword.route) { ResetPasswordScreen(navController) }
    }
}