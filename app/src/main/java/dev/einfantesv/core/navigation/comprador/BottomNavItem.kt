package dev.einfantesv.core.navigation.comprador

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Inicio")
    object Profile : BottomNavItem("profile", Icons.Default.AccountBox, "Perfil")
    object Promotions : BottomNavItem("promotions", Icons.Default.Star, "Promociones")
    object SignUpVendedor : BottomNavItem("signUpVendedor", Icons.Default.Star, "Registrame como vendedor")
    object OrdenesCompradorScreen : BottomNavItem("ordenesCompradorScreen", Icons.Default.ShoppingBag, "Mis Compras")
}