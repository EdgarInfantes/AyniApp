package dev.einfantesv.screens

import android.util.Log
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF00C853),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = busquedaText,
                    onValueChange = { busquedaText = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    label = { Text("Buscar", color = Color(0xFF00C853)) },
                    placeholder = { Text("Pan con pollo", color = Color(0xFFB5B0AD)) },
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            RecomendacionDelDia()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nuestros Vendedores:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(vendedores) { vendedor ->
            VendedorCard(vendedor = vendedor) {
                Log.d("Vendedor", "Viendo productos del vendedor: ${vendedor.uid}")
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

@Composable
fun RecomendacionDelDia() {
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
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
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
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Precio",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(it.precio)
                    }
                }
            }
        }
    }
}
