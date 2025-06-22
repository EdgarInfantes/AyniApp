package dev.einfantesv.presentation.auth.vendedor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.einfantesv.models.ProductoFirebase

@Composable
fun GestionProductosScreen(
    productos: List<ProductoFirebase>,
    onAgregarProducto: (ProductoFirebase) -> Unit,
    onActualizarProducto: (ProductoFirebase) -> Unit
) {
    var productoEditando by remember { mutableStateOf<ProductoFirebase?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mis Productos", fontSize = 22.sp, color = Color(0xFF00C853))
        Spacer(modifier = Modifier.height(12.dp))

        AgregarProductoForm(onAgregar = { nombre, precio, imagen ->
            onAgregarProducto(ProductoFirebase(nombre = nombre, precio = precio, imagen = imagen))
        })

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(productos) { producto ->
                ProductoCard(producto = producto) {
                    productoEditando = it
                }
            }
        }

        productoEditando?.let { producto ->
            Dialog(onDismissRequest = { productoEditando = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    var nombre by remember { mutableStateOf(producto.nombre) }
                    var precio by remember { mutableStateOf(producto.precio) }
                    var imagen by remember { mutableStateOf(producto.imagen) }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Editar Producto", fontSize = 18.sp, color = Color(0xFF00C853))
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = precio,
                            onValueChange = { precio = it },
                            label = { Text("Precio") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = imagen,
                            onValueChange = { imagen = it },
                            label = { Text("Imagen URL") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { productoEditando = null }) {
                                Text("Cancelar")
                            }
                            Button(
                                onClick = {
                                    onActualizarProducto(
                                        producto.copy(nombre = nombre, precio = precio, imagen = imagen)
                                    )
                                    productoEditando = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                            ) {
                                Text("Guardar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: ProductoFirebase, onClick: (ProductoFirebase) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick(producto) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(producto.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Precio: ${producto.precio}")
            Text("Imagen URL: ${producto.imagen}", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AgregarProductoForm(
    onAgregar: (nombre: String, precio: String, imagenUrl: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Agregar Producto",
            fontSize = 20.sp,
            color = Color(0xFF00C853),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio (ej. 3.50)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = imagenUrl,
            onValueChange = { imagenUrl = it },
            label = { Text("URL de la imagen") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (nombre.isNotBlank() && precio.isNotBlank() && imagenUrl.isNotBlank()) {
                    onAgregar(nombre, precio, imagenUrl)
                    nombre = ""
                    precio = ""
                    imagenUrl = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Agregar", color = Color.White)
        }
    }
}
