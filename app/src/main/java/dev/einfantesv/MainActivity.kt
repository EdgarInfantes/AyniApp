package dev.einfantesv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.einfantesv.core.navigation.NavigationWrapper
import dev.einfantesv.ui.theme.AyniAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AyniAppTheme {
                NavigationWrapper()
            }
        }
    }
}

