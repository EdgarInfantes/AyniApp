package dev.einfantesv.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.tasks.await
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.einfantesv.models.VendedorFirebase
import kotlinx.coroutines.tasks.await
import java.util.*


@Composable
fun AdminVendedorScreen(navController: NavHostController) {
    val context = LocalContext.current
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    var vendedor by remember { mutableStateOf<VendedorFirebase?>(null) }
    var contacto by remember { mutableStateOf("") }
    val horarios = remember { mutableStateMapOf<String, Pair<String, String>>() }
    val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("Vendedores")
                .document(uid)
                .get()
                .await()
            vendedor = snapshot.toObject(VendedorFirebase::class.java)
            contacto = vendedor?.contacto ?: ""
            snapshot.get("horarios")?.let { raw ->
                if (raw is Map<*, *>) {
                    raw.forEach { (dia, value) ->
                        if (dia is String && value is Map<*, *>) {
                            val inicio = value["inicio"] as? String ?: ""
                            val fin = value["fin"] as? String ?: ""
                            horarios[dia] = inicio to fin
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error al cargar datos: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Administración del Vendedor", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Negocio: ${vendedor?.nombre ?: "No disponible"}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contacto,
                onValueChange = { contacto = it },
                label = { Text("Teléfono o contacto") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Horarios de atención", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            diasSemana.forEach { dia ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(dia, modifier = Modifier.weight(1f))
                    Button(onClick = {
                        val c = Calendar.getInstance()
                        TimePickerDialog(context, { _, h1, m1 ->
                            val inicio = "%02d:%02d".format(h1, m1)
                            TimePickerDialog(context, { _, h2, m2 ->
                                val fin = "%02d:%02d".format(h2, m2)
                                horarios[dia] = inicio to fin
                            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
                    }) {
                        Text("Editar")
                    }
                }
                horarios[dia]?.let {
                    Text("${it.first} - ${it.second}", fontSize = 14.sp, color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val data = mapOf(
                    "contacto" to contacto,
                    "horarios" to horarios.mapValues { mapOf("inicio" to it.value.first, "fin" to it.value.second) }
                )
                FirebaseFirestore.getInstance()
                    .collection("Vendedores")
                    .document(uid)
                    .update(data)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Información actualizada", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error al guardar: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))) {
                Text("Guardar cambios", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { navController.navigate("agregarProducto") }) {
                Text("Registrar productos para mañana")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("pedidosActivos") }) {
                Text("Ver pedidos activos")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("ventasPasadas") }) {
                Text("Historial de ventas")
            }
        }
    }
}
