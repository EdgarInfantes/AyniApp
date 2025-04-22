package dev.einfantesv.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

//Para la navegacion entre pantallas
sealed class Screens(val route: String) {
    object Login : Screens("login")
    object SignUp : Screens("signup")
    object Home : Screens("home")
    object ResetPassword : Screens("reset_password")
}
