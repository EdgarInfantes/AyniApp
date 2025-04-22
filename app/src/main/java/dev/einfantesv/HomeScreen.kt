package dev.einfantesv.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import dev.einfantesv.R

@Composable
fun HomeScreen(navController: NavHostController) {
    // Datos de ejemplo
    val recomendacion = Producto("Pan Con Pollo", "S/ 3.50", "10 min", R.drawable.recodeldia)

    // Datos de ejemplo
    val vendedores = listOf(
        Vendedor(
            "Carla Castilla",
            "4.8 ★",
            "15-20 min",
            "Disponible hasta 22:00",
            R.drawable.logovendedor
        ),
        Vendedor(
            "Zharick Sanchez",
            "4.7 ★",
            "10-15 min",
            "Disponible hasta 16:30",
            R.drawable.logovendedor2
        ),
        Vendedor(
            "Diego Rosales",
            "4.7 ★",
            "10-15 min",
            "Disponible hasta 19:30",
            R.drawable.logovendedor3
        ),
    )

    var busquedaText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        //Espacer
        Spacer(modifier = Modifier.width(16.dp))

        // Sección de saludo
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Hola, ")
                    withStyle(style = SpanStyle(color = Color(0xFF00C853), fontWeight = FontWeight.Bold)) {
                        append("Carla")
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

        // Barra de búsqueda
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

        // Recomendación del día
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
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5) // Fondo gris claro
            )
        ) {
            Column {
                Image(
                    painter = painterResource(id = recomendacion.imagenId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = recomendacion.nombre,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Precio",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(recomendacion.precio)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "Tiempo",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(recomendacion.tiempo)
                    }
                }
            }
        }

        // Sección de vendedores
        Text(
            text = "Nuestros Vendedores:",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        vendedores.forEach { vendedor ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(180.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Imagen de fondo del vendedor
                    Image(
                        painter = painterResource(id = vendedor.imagenId),
                        contentDescription = "Logo ${vendedor.nombre}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        alpha = 0.7f
                    )

                    // Capa oscura para mejorar contraste del texto
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                    )

                    // Contenido sobre la imagen
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Nombre y calificación
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

                        // Tiempo de entrega
                        Text(
                            text = "⏱ ${vendedor.tiempoEntrega}",
                            color = Color.White,
                            fontSize = 16.sp
                        )

                        // Horario disponible con fondo verde
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
    }
}
// Modelos de datos
data class Producto(
    val nombre: String,
    val precio: String,
    val tiempo: String,
    val imagenId: Int
)

data class Vendedor(
    val nombre: String,
    val calificacion: String,
    val tiempoEntrega: String,
    val horarioDisponible: String,
    val imagenId: Int
)