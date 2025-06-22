package dev.einfantesv.presentation.auth.vendedor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Orden(
    val id: String,
    val producto: String,
    val estado: String, // "Activa", "Completada", "Cancelada"
    val fecha: String,
    val total: String
)

@Composable
fun OrdenesListScreen(
    ordenes: List<Orden>
) {
    var filtro by remember { mutableStateOf("Todas") }

    val ordenesFiltradas = when (filtro) {
        "Activas" -> ordenes.filter { it.estado == "Activa" }
        "Completadas" -> ordenes.filter { it.estado == "Completada" }
        "Canceladas" -> ordenes.filter { it.estado == "Cancelada" }
        else -> ordenes
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Botones de filtro
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Activas", "Todas", "Completadas", "Canceladas").forEach { estado ->
                FilterChip(
                    selected = filtro == estado,
                    onClick = { filtro = estado },
                    label = {
                        Text(estado, color = if (filtro == estado) Color.White else Color.Black)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF00C853),
                        containerColor = Color.LightGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de órdenes
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(ordenesFiltradas) { orden ->
                OrdenCard(orden)
            }
        }
    }
}

@Composable
fun OrdenCard(orden: Orden) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Producto: ${orden.producto}", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text("Fecha: ${orden.fecha}", fontSize = 14.sp)
            Text("Total: ${orden.total}", fontSize = 14.sp)
            Text("Estado: ${orden.estado}", fontSize = 14.sp, color =
                when (orden.estado) {
                    "Activa" -> Color(0xFF00C853)
                    "Completada" -> Color.Gray
                    "Cancelada" -> Color.Red
                    else -> Color.Black
                }
            )
        }
    }
}
