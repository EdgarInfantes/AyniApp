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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.filled.Remove
import dev.einfantesv.firebase.FirebaseGetDataManager
import dev.einfantesv.models.ProductoFirebase
import dev.einfantesv.util.AnimatedSnackbar

@Composable
fun SeleccionarCantidadScreen(productoId: String, navController: NavHostController) {
    var producto by remember { mutableStateOf<ProductoFirebase?>(null) }
    var cantidad by remember { mutableStateOf(1) }
    var nota by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarColor by remember { mutableStateOf(Color.Green) }


    LaunchedEffect(productoId) {
        FirebaseGetDataManager.getProductoPorId(productoId) {
            producto = it
        }
    }

    // Manejamos la lógica de delay y navegación
    LaunchedEffect(snackbarVisible) {
        if (snackbarVisible) {
            kotlinx.coroutines.delay(2000)
            snackbarVisible = false
            if (mensaje == "Pedido realizado con éxito") {
                navController.popBackStack()
            }
        }
    }

    producto?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            val painter = rememberAsyncImagePainter(model = it.imagen)
            Box {
                Image(
                    painter = painter,
                    contentDescription = it.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(it.nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Costo del producto: S/${it.precio}", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Text("Cantidad", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF00C853))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { if (cantidad > 1) cantidad-- },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Menos", tint = Color.Gray)
                }

                Text(cantidad.toString(), fontSize = 20.sp, modifier = Modifier.padding(horizontal = 24.dp))

                IconButton(
                    onClick = { cantidad++ },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Más", tint = Color(0xFF00C853))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entregar en:", fontSize = 16.sp, color = Color(0xFF00C853))

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it },
                    placeholder = { Text("A-302", color = Color.Gray) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00C853),
                        unfocusedBorderColor = Color(0xFF00C853),
                        focusedLabelColor = Color(0xFF00C853),
                        cursorColor = Color(0xFF00C853)
                    )

                )

            }

            /*
            Text("Entregar en:", fontSize = 16.sp, color = Color.White)
            OutlinedTextField(
                value = nota,
                onValueChange = { nota = it },
                label = { Text("A-302") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

             */

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (nota.isBlank()) {
                        mensaje = "Debe ingresar el lugar de entrega"
                        snackbarColor = Color(0xFFF44336) // rojo para error
                        snackbarVisible = true
                        //return@Button
                    } else {
                        FirebaseGetDataManager.realizarPedido(it, cantidad, nota) { exito ->
                            if (exito) {
                                mensaje = "Pedido realizado con éxito"
                                snackbarColor = Color(0xFF00C853) // verde para éxito
                            } else {
                                mensaje = "Error al realizar el pedido"
                                snackbarColor = Color(0xFFF44336) // rojo para error
                            }
                            snackbarVisible = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Comprar", fontSize = 16.sp, color = Color.White)
            }



            /*
            Button(
                onClick = {
                    FirebaseGetDataManager.realizarPedido(it, cantidad, nota) { exito ->
                        if (exito) {
                            mensaje = "Pedido realizado con éxito"
                            snackbarVisible = true
                        } else {
                            mensaje = "Error al realizar el pedido"
                            snackbarVisible = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Comprar", fontSize = 16.sp, color = Color.White)
            }

             */
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF00C853))
    }

    // Snackbar siempre al final
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedSnackbar(
            visible = snackbarVisible,
            message = mensaje,
            backgroundColor = snackbarColor
        )
    }

}
