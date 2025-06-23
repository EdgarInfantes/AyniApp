package dev.einfantesv.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import dev.einfantesv.R
import dev.einfantesv.UserSessionViewModel
import dev.einfantesv.firebase.FirebaseGetDataManager
import dev.einfantesv.models.ProductoFirebase
import dev.einfantesv.models.VendedorFirebase
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel
) {
    var busquedaText by remember { mutableStateOf("") }
    var vendedores by remember { mutableStateOf<List<VendedorFirebase>>(emptyList()) }
    val userData by userSessionViewModel.userData.collectAsState()
    val nombre = userData?.nombre ?: "Usuario"

    LaunchedEffect(Unit) {
        FirebaseGetDataManager.getVendedores { lista ->
            vendedores = lista
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .imePadding(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = buildAnnotatedString {
                        append("Hola, ")
                        withStyle(style = SpanStyle(color = Color(0xFF00C853), fontWeight = FontWeight.Bold)) {
                            append(nombre)
                        }
                    },
                    fontSize = 24.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RecomendacionDelDia(navController)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nuestros Vendedores:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(vendedores) { vendedor ->
            VendedorCard(vendedor = vendedor) {
                navController.navigate("productos_vendedor/${vendedor.uid}")
            }
        }
    }
}

@Composable
fun RecomendacionDelDia(navController: NavHostController) {
    var producto by remember { mutableStateOf<ProductoFirebase?>(null) }

    LaunchedEffect(Unit) {
        FirebaseGetDataManager.getRecomendacionDelDia { fetchedProducto ->
            producto = fetchedProducto
        }
    }

    producto?.let {
        Column {
            Text(
                text = "Recomendación del día",
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        val encodedNombre = java.net.URLEncoder.encode(it.nombre, "UTF-8")
                        navController.navigate("vendedores_que_venden/$encodedNombre")
                    },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(it.imagen),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = it.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Precio: S/${it.precio}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }
}

// Archivo: VendedoresQueVendenScreen.kt

@Composable
fun VendedoresQueVendenScreen(nombreProducto: String, navController: NavHostController) {
    val nombreDecodificado = URLDecoder.decode(nombreProducto, StandardCharsets.UTF_8.toString())
    var vendedores by remember { mutableStateOf<List<VendedorFirebase>>(emptyList()) }

    LaunchedEffect(nombreDecodificado) {
        FirebaseGetDataManager.getVendedoresQueVendenProducto(
            nombreProducto = nombreDecodificado,
            onComplete = { lista -> vendedores = lista }
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp))
    {
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Vendedores que ofrecen $nombreDecodificado",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF00C853)
                )
            }
        }


        Spacer(Modifier.height(24.dp))

        // Lista de vendedores
        vendedores.forEach { vendedor ->
            VendedorCard(vendedor = vendedor) {
                navController.navigate("productos_vendedor/${vendedor.uid}")
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
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painter,
                contentDescription = "Logo ${vendedor.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.7f
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            if (painter.state is coil.compose.AsyncImagePainter.State.Error) {
                Text(
                    "Imagen no disponible",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = vendedor.nombre,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = vendedor.calificacion,
                        color = Color.Yellow,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "⏱ ${vendedor.tiempoEntrega}",
                    color = Color.White,
                    fontSize = 16.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF00C853), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = vendedor.horarioDisponible,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
