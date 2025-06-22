package dev.einfantesv.presentation.auth.vendedor

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun EditarHorariosScreen(
    horariosIniciales: Map<String, Pair<String, String>> = emptyMap(),
    onGuardar: (Map<String, Pair<String, String>>) -> Unit
) {
    val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val horarios = remember { mutableStateMapOf<String, Pair<String, String>>() }

    // Cargar horarios iniciales si existen
    LaunchedEffect(horariosIniciales) {
        horarios.putAll(horariosIniciales)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Editar Horarios", fontSize = 22.sp, color = Color(0xFF00C853))

        Spacer(modifier = Modifier.height(8.dp))

        diasSemana.forEach { dia ->
            HorarioSelector(
                dia = dia,
                horarioActual = horarios[dia],
                onHorarioSeleccionado = { inicio, fin ->
                    horarios[dia] = inicio to fin
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onGuardar(horarios.toMap()) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Guardar", color = Color.White)
        }
    }
}


@Composable
fun HorarioSelector(
    dia: String,
    horarioActual: Pair<String, String>?,
    onHorarioSeleccionado: (String, String) -> Unit
) {
    val context = LocalContext.current
    val c = Calendar.getInstance()

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = dia,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp
            )

            Button(
                onClick = {
                    TimePickerDialog(
                        context,
                        { _, h1, m1 ->
                            val inicio = "%02d:%02d".format(h1, m1)
                            TimePickerDialog(
                                context,
                                { _, h2, m2 ->
                                    val fin = "%02d:%02d".format(h2, m2)
                                    onHorarioSeleccionado(inicio, fin)
                                },
                                c.get(Calendar.HOUR_OF_DAY),
                                c.get(Calendar.MINUTE),
                                true
                            ).show()
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
            ) {
                Text("Definir horario", color = Color.White)
            }
        }

        horarioActual?.let {
            Text(
                text = "${it.first} - ${it.second}",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
