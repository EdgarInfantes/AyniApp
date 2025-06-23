package dev.einfantesv.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import dev.einfantesv.firebase.FirebaseGetDataManager
import dev.einfantesv.models.ProductoFirebase

@Composable
fun ProductosDelVendedorScreen(vendedorId: String, navController: NavHostController) {
    var productos by remember { mutableStateOf<List<ProductoFirebase>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(vendedorId) {
        FirebaseGetDataManager.getProductosPorVendedor(vendedorId) {
            productos = it
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
            ) {
                Text("← Volver", color = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Productos del vendedor",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00C853)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF00C853))
            }
        } else if (productos.isEmpty()) {
            Text("Este vendedor aún no tiene productos registrados.")
        } else {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                productos.forEach { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("seleccionarCantidad/${producto.id}")
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val imagePainter = rememberAsyncImagePainter(model = producto.imagen)

                            Image(
                                painter = imagePainter,
                                contentDescription = producto.nombre,
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(end = 12.dp),
                                contentScale = ContentScale.Crop
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = producto.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Precio: ${producto.precio}",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
