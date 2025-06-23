package dev.einfantesv.presentation.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import dev.einfantesv.firebase.FirebaseAuthManager
import dev.einfantesv.firebase.FirebaseGetDataManager
import dev.einfantesv.firebase.FirebaseProductoManager
import dev.einfantesv.models.ProductoFirebase
import dev.einfantesv.presentation.auth.vendedor.EditarHorariosScreen
import dev.einfantesv.presentation.auth.vendedor.GestionProductosScreen
import dev.einfantesv.util.AnimatedSnackbar
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
            BottomNavigationBarPersonalizado(
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    if (index == 1) {
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
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> if (isLoading) LoadingScreen() else GestionProductosScreen(
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
                1 -> if (ordenesLoading) LoadingScreen() else OrdenesScreen(ordenes = ordenes)
                2 -> EditarHorariosScreen {
                    Toast.makeText(context, "Horarios guardados correctamente", Toast.LENGTH_SHORT).show()
                }
                3 -> PerfilTab {
                    FirebaseAuthManager.signOut()
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") { popUpTo(0) }
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
fun OrdenesScreen(ordenes: List<Map<String, Any>>) {
    val context = LocalContext.current
    var mensaje by remember { mutableStateOf("") }
    var snackbarVisible by remember { mutableStateOf(false) }

    // Oculta el snackbar automáticamente después de 2 segundos
    LaunchedEffect(snackbarVisible) {
        if (snackbarVisible) {
            kotlinx.coroutines.delay(2000)
            snackbarVisible = false
        }
    }

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
                var imagenUrl by remember { mutableStateOf<String?>(null) }
                var nombreComprador by remember { mutableStateOf("Cargando...") }

                val uidComprador = orden["uidComprador"] as? String
                val productoId = orden["productoId"] as? String
                val cantidad = orden["cantidad"]?.toString() ?: "1"
                val total = orden["precioTotal"]?.toString() ?: "0.0"
                val ordenId = orden["id"] as? String

                // Estado local para refrescar visualmente si fue entregado
                var estado by remember { mutableStateOf(orden["estado"] as? String ?: "Pendiente") }

                val fecha = (orden["fechaHora"] as? com.google.firebase.Timestamp)?.toDate()
                val fechaFormateada = fecha?.let {
                    java.text.SimpleDateFormat("EEE dd MMM • hh:mm a", java.util.Locale("es"))
                        .format(it)
                } ?: "Sin fecha"

                // Obtener comprador
                LaunchedEffect(uidComprador) {
                    if (uidComprador != null) {
                        FirebaseGetDataManager.getNombre("Compradores", uidComprador) {
                            nombreComprador = it ?: "Desconocido"
                        }
                    }
                }

                // Obtener imagen
                LaunchedEffect(productoId) {
                    if (productoId != null) {
                        FirebaseGetDataManager.getProductoPorId(productoId) {
                            imagenUrl = it?.imagen
                        }
                    }
                }

                // Tarjeta
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, Color(0xFFFFEBEE))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (!imagenUrl.isNullOrEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(imagenUrl),
                                    contentDescription = "Imagen del producto",
                                    modifier = Modifier
                                        .size(64.dp)
                                        .padding(end = 12.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Estado: $estado", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text("Comprador • $nombreComprador", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Entregado • $fechaFormateada", fontSize = 14.sp)
                                Text("S/$total • $cantidad producto(s)", fontSize = 14.sp)
                            }
                        }

                        // Botón solo si está pendiente
                        if (estado == "Pendiente" && ordenId != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    FirebaseFirestore.getInstance()
                                        .collection("Pedidos")
                                        .document(ordenId)
                                        .update("estado", "Entregado")
                                        .addOnSuccessListener {
                                            estado = "Entregado"
                                            mensaje = "Pedido marcado como entregado"
                                            snackbarVisible = true
                                        }
                                        .addOnFailureListener {
                                            mensaje = "Error al actualizar el pedido"
                                            snackbarVisible = true
                                        }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                            ) {
                                Text("Marcar como entregado", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedSnackbar(
            visible = snackbarVisible,
            message = mensaje,
            backgroundColor = Color(0xFF00C853)
        )
    }
}




@Composable
fun BottomNavigationBarPersonalizado(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf("Productos", "Órdenes", "Horarios", "Perfil")
    val icons = listOf(
        Icons.Default.ShoppingCart,
        Icons.Default.Receipt,
        Icons.Default.Schedule,
        Icons.Default.Person
    )

    Surface(
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onItemSelected(index) }
                ) {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item,
                        tint = if (isSelected) Color(0xFF00C853) else Color.Gray
                    )
                    Text(
                        text = item,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color(0xFF00C853) else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF00C853))
    }
}
