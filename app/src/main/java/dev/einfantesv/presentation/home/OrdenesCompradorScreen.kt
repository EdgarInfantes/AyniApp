package dev.einfantesv.presentation.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dev.einfantesv.UserSessionViewModel
import dev.einfantesv.firebase.FirebaseGetDataManager
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrdenesCompradorScreen(
    mainNavController: NavHostController,
    userSessionViewModel: UserSessionViewModel
) {
    val uidComprador = FirebaseAuth.getInstance().currentUser?.uid
    var ordenes by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    LaunchedEffect(uidComprador) {
        if (uidComprador != null) {
            FirebaseGetDataManager.getOrdenesDeComprador(
                compradorId = uidComprador,
                onComplete = {
                    Log.e("OrdenesComprador", "UID comprador: $uidComprador")
                    ordenes = it
                },
                onError = { e ->
                    Log.e("OrdenesComprador", "Error al obtener pedidos: ${e.message}")
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Mis Compras", fontSize = 22.sp, color = Color(0xFF00C853))
        Spacer(modifier = Modifier.height(12.dp))

        if (ordenes.isEmpty()) {
            Text("No has realizado pedidos aún.")
        } else {
            ordenes.forEach { orden ->
                var imagenUrl by remember { mutableStateOf<String?>(null) }
                var nombreVendedor by remember { mutableStateOf("Cargando...") }

                val uidVendedor = orden["uidVendedor"] as? String
                val productoId = orden["productoId"] as? String
                val cantidad = orden["cantidad"]?.toString() ?: "1"
                val total = orden["precioTotal"]?.toString() ?: "0.0"
                val estado = orden["estado"] as? String ?: "Pendiente"
                val nota = orden["nota"] as? String

                val fecha = (orden["fechaHora"] as? Timestamp)?.toDate()
                val fechaFormateada = fecha?.let {
                    SimpleDateFormat("EEE dd MMM • hh:mm a", Locale("es")).format(it)
                } ?: "Sin fecha"

                LaunchedEffect(uidVendedor) {
                    if (uidVendedor != null) {
                        FirebaseGetDataManager.getNombre("Vendedores", uidVendedor) {
                            nombreVendedor = it ?: "Desconocido"
                        }
                    }
                }

                LaunchedEffect(productoId) {
                    if (productoId != null) {
                        FirebaseGetDataManager.getProductoPorId(productoId) {
                            imagenUrl = it?.imagen
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, Color(0xFFFFEBEE))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
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
                            Text("$estado • $fechaFormateada", fontSize = 14.sp)
                            Text("Vendedor: $nombreVendedor", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Lugar de entrega: $nota", fontSize = 14.sp)
                            Text("S/$total • $cantidad producto(s)", fontSize = 14.sp)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { /* Acción: opinar */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
                                ) {
                                    Text("Opinar", color = Color.White)
                                }

                                Button(
                                    onClick = { /* Acción: repetir */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
                                ) {
                                    Text("Repetir", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
