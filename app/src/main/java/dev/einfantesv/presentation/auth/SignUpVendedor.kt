package dev.einfantesv.presentation.auth

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dev.einfantesv.UserSessionViewModel
import dev.einfantesv.core.navigation.Screens
import dev.einfantesv.firebase.FirebaseAuthManager
import dev.einfantesv.models.TempUserData
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun SignUpVendedorScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userSessionViewModel: UserSessionViewModel = viewModel()
    val userData by userSessionViewModel.userData.collectAsState()


    var contacto by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    val horarios = remember { mutableStateMapOf<String, Pair<String, String>>() }
    val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registrarme como Vendedor",
            fontSize = 24.sp,
            color = Color(0xFF00C853)
        )

        Spacer(modifier = Modifier.height(16.dp))

        /*
        Text("Nombre del negocio:", fontSize = 16.sp)
        Text(
            text = nombreNegocio,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

         */

        OutlinedTextField(
            value = contacto,
            onValueChange = { contacto = it },
            label = { Text("Número de celular") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("URL del logo del negocio") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Horarios de atención (opcional)", fontSize = 16.sp)

        diasSemana.forEach { dia ->
            DiaHorarioPicker(
                dia = dia,
                horarios = horarios
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (contacto.isBlank()) {
                    Toast.makeText(context, "Completa el número de celular", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    try {
                        val result = FirebaseAuthManager.registerAsVendor(
                            nombre = TempUserData.nombre,
                            apellido = TempUserData.apellido,
                            email = TempUserData.email,
                            descripcion = "", // pendiente
                            contacto = contacto,
                            horarios = horarios.toMap(),
                            logoUrl = imageUrl,
                        )

                        if (result.isSuccess) {
                            Toast.makeText(context, "Registrado como vendedor", Toast.LENGTH_LONG).show()
                            navController.navigate(Screens.Login.route)
                        } else {
                            Toast.makeText(
                                context,
                                "Error: ${result.exceptionOrNull()?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
        ) {
            Text("Registrarme como Vendedor", color = Color.White)
        }
    }
}

@Composable
private fun DiaHorarioPicker(
    dia: String,
    horarios: MutableMap<String, Pair<String, String>>
) {
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(dia, modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val c = Calendar.getInstance()
                    TimePickerDialog(context, { _, h1, m1 ->
                        val inicio = "%02d:%02d".format(h1, m1)
                        TimePickerDialog(context, { _, h2, m2 ->
                            val fin = "%02d:%02d".format(h2, m2)
                            horarios[dia] = inicio to fin
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
                    }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
            ) {
                Text("Definir", color = Color.White)
            }
        }

        horarios[dia]?.let { (inicio, fin) ->
            Text(
                text = "$inicio - $fin",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
