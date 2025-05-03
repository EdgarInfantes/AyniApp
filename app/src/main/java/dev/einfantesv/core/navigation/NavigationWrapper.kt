package dev.einfantesv.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import dev.einfantesv.LoginScreen
import dev.einfantesv.ResetPasswordScreen
import dev.einfantesv.SignUpScreen
import dev.einfantesv.SignUpScreen2
import dev.einfantesv.SignUpScreen3


@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Login.route) {
        composable(Screens.Login.route) { LoginScreen(navController) }
        composable(Screens.Home.route) { HomeNavigation(navController) }
        composable(Screens.SignUp.route) { SignUpScreen(navController) }
        composable(Screens.SignUp2.route) { SignUpScreen2(navController) }
        composable(Screens.SignUp3.route) { SignUpScreen3(navController) }
        composable(Screens.ResetPassword.route) { ResetPasswordScreen(navController) }
    }
}