package dev.einfantesv.core.navigation

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object SignUp : Screens("signup")
    object Home : Screens("home")
    object ResetPassword : Screens("reset_password")
}
