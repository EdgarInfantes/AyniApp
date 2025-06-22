package dev.einfantesv.core.navigation.vendedor

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItemVendedor(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItemVendedor("home", Icons.Default.Home, "Inicio")
    object Profile : BottomNavItemVendedor("profile", Icons.Default.AccountBox, "Perfil")
    object Promotions : BottomNavItemVendedor("promotions", Icons.Default.Star, "Promociones")
    object AdminVendedor : BottomNavItemVendedor("adminVendedor", Icons.Default.Star, "Administrar mi negocio")
}