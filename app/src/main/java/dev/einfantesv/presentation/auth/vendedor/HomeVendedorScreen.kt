package dev.einfantesv.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dev.einfantesv.firebase.FirebaseAuthManager
import dev.einfantesv.firebase.FirebaseGetDataManager
import dev.einfantesv.firebase.FirebaseProductoManager
import dev.einfantesv.models.ProductoFirebase
import dev.einfantesv.presentation.auth.vendedor.EditarHorariosScreen
import dev.einfantesv.presentation.auth.vendedor.GestionProductosScreen
import kotlinx.coroutines.launch

@Composable
fun HomeVendedorScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }

    var productos by remember { mutableStateOf<List<ProductoFirebase>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var ordenes by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var ordenesLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            val result = FirebaseProductoManager.obtenerMisProductos()
            if (result.isSuccess) {
                productos = result.getOrDefault(emptyList())
            }
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavigationButton("Productos", selected = selectedTab == 0) { selectedTab = 0 }
                NavigationButton("Órdenes", selected = selectedTab == 1) {
                    selectedTab = 1
                    ordenesLoading = true
                    FirebaseAuthManager.getCurrentUser()?.uid?.let { uid ->
                        FirebaseGetDataManager.getOrdenesDeVendedor(
                            vendedorId = uid,
                            onComplete = {
                                ordenes = it
                                ordenesLoading = false
                            },
                            onError = {
                                Toast.makeText(context, "Error al cargar órdenes", Toast.LENGTH_SHORT).show()
                                ordenesLoading = false
                            }
                        )
                    }
                }
                NavigationButton("Horarios", selected = selectedTab == 2) { selectedTab = 2 }
                NavigationButton("Perfil", selected = selectedTab == 3) { selectedTab = 3 }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> {
                    if (isLoading) {
                        LoadingScreen()
                    } else {
                        GestionProductosScreen(
                            productos = productos,
                            onAgregarProducto = { nuevo ->
                                scope.launch {
                                    val res = FirebaseProductoManager.agregarProducto(
                                        nuevo.nombre, nuevo.precio, nuevo.imagen
                                    )
                                    if (res.isSuccess) {
                                        productos = productos + nuevo
                                        Toast.makeText(context, "Producto agregado", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error al agregar", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            onActualizarProducto = { actualizado ->
                                scope.launch {
                                    val res = FirebaseProductoManager.actualizarProducto(actualizado)
                                    if (res.isSuccess) {
                                        productos = productos.map {
                                            if (it.id == actualizado.id) actualizado else it
                                        }
                                        Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        )
                    }
                }

                1 -> {
                    if (ordenesLoading) {
                        LoadingScreen()
                    } else {
                        OrdenesScreen(ordenes = ordenes)
                    }
                }

                2 -> EditarHorariosScreen(
                    onGuardar = {
                        Toast.makeText(context, "Horarios guardados correctamente", Toast.LENGTH_SHORT).show()
                    }
                )

                3 -> PerfilTab(
                    onLogout = {
                        FirebaseAuthManager.signOut()
                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NavigationButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF00C853) else Color.LightGray,
            contentColor = if (selected) Color.White else Color.DarkGray
        )
    ) {
        Text(text)
    }
}

@Composable
fun OrdenesScreen(ordenes: List<Map<String, Any>>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Mis Órdenes", fontSize = 22.sp, color = Color(0xFF00C853))
        Spacer(modifier = Modifier.height(12.dp))

        if (ordenes.isEmpty()) {
            Text("No tienes órdenes aún.")
        } else {
            ordenes.forEach { orden ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("ID: ${orden["id"] ?: "N/A"}", fontSize = 14.sp)
                        Text("Fecha: ${orden["fecha"]}", fontSize = 14.sp)
                        Text("Estado: ${orden["estado"] ?: "Desconocido"}", fontSize = 14.sp)
                        // Puedes agregar más campos aquí si están disponibles
                    }
                }
            }
        }
    }
}

@Composable
fun PerfilTab(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mi Perfil", fontSize = 22.sp, color = Color(0xFF00C853))
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
        ) {
            Text("Cerrar Sesión", color = Color.White)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF00C853))
    }
}
