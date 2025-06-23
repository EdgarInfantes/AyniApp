package dev.einfantesv.presentation.home

import java.net.URLDecoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import dev.einfantesv.firebase.FirebaseGetDataManager
import dev.einfantesv.models.VendedorFirebase
import java.nio.charset.StandardCharsets

@Composable
fun VendedoresQueVendenScreen2(nombreProducto: String, navController: NavHostController) {
    val nombreDecodificado = URLDecoder.decode(nombreProducto, StandardCharsets.UTF_8.toString())
    var vendedores by remember { mutableStateOf<List<VendedorFirebase>>(emptyList()) }

    LaunchedEffect(nombreDecodificado) {
        FirebaseGetDataManager.getVendedoresQueVendenProducto(
            nombreProducto = nombreDecodificado,
            onComplete = { lista -> vendedores = lista }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Vendedores que ofrecen \"$nombreDecodificado\"",
            fontSize = 20.sp,
            color = Color(0xFF00C853)
        )

        Spacer(Modifier.height(16.dp))

        if (vendedores.isEmpty()) {
            Text("Ningún vendedor ofrece este producto por ahora.")
        } else {
            vendedores.forEach { vendedor ->
                VendedorCard(vendedor = vendedor) {
                    navController.navigate("productos_vendedor/${vendedor.uid}")
                }
            }
        }
    }
}

@Composable
fun VendedorCard(vendedor: VendedorFirebase, onClick: () -> Unit) {
    val painter = rememberAsyncImagePainter(model = vendedor.imagen)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.height(180.dp)) {
            Image(
                painter = painter,
                contentDescription = "Imagen de ${vendedor.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.8f)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(vendedor.nombre, fontSize = 20.sp, color = Color.White)
                Text("⏱ ${vendedor.tiempoEntrega}", color = Color.White)
                Text("⭐ ${vendedor.calificacion}", color = Color.Yellow)
                Box(
                    modifier = Modifier
                        .background(Color(0xFF00C853), RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    Text(
                        text = vendedor.horarioDisponible,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
