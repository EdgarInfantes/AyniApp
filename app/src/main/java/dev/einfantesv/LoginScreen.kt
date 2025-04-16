package dev.einfantesv

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import dev.einfantesv.core.navigation.Screens

@Composable
fun LoginScreen(navController: NavHostController){
    Column(        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Logo"
        )
        Button(
            onClick = { navController.navigate(Screens.Home.route) }
        ) {
            Text("Ir a Home")
        }
    }
}