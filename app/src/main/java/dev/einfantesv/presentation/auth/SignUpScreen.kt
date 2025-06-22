package dev.einfantesv.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dev.einfantesv.R
import dev.einfantesv.core.navigation.Screens
import dev.einfantesv.models.TempUserData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpScreen(navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        //Variables del LoginScreen
        var emailText by remember { mutableStateOf("") }
        var valEmailText by remember { mutableStateOf("") }
        val isKeyboardVisible = WindowInsets.isImeVisible

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Nombre de la APP
            Text(
                text = "Bienvenido a",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            //Texto de ayni color rojo:
            Text(
                text = "AyniApp",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF00C853),
                fontWeight = FontWeight.Bold
            )
            // Separador
            Spacer(modifier = Modifier.height(16.dp))

            //Imagen del logo
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Logo",
                modifier = Modifier.size(300.dp)
            )
            // Separador
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(70.dp)
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF00C853),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                //TextField: Usuario
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = emailText,
                    onValueChange = { emailText = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    label = { Text("Correo", color = Color(0xFF00C853)) },
                    placeholder = { Text("usuario@dominio.com", color = Color(0xFFB5B0AD)) }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(70.dp)
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF00C853),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                //TextField: Usuario
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = valEmailText,
                    onValueChange = { valEmailText = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    label = { Text("Verifica tu correo", color = Color(0xFF00C853)) },
                    placeholder = { Text("usuario@dominio.com", color = Color(0xFFB5B0AD)) }
                )
            }

            // Separador
            Spacer(modifier = Modifier.height(5.dp))

            //Poner alerta de correos no coincidentes
            if (!isKeyboardVisible && emailText != valEmailText) {
                Text(
                    text = "Los correos no coinciden",
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            //Button Iniciar Sesión
            Button(
                onClick = { navController.navigate(Screens.SignUp2.route) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C853),
                    contentColor = Color.White
                ),
                enabled = emailText == valEmailText && emailText.isNotBlank() && valEmailText.isNotBlank(),
            ) {
                TempUserData.email = emailText

                Text("Siguiente",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

            }

            //Fila Ya Tiene una cuenta
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
            {
                Text(
                    text = "¿Ya tienes una cuenta?"
                )
                // Separador
                Spacer(modifier = Modifier.width(5.dp))

                //Texto Clickeale para mandar al SignUpScreen
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF00C853),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { navController.navigate(Screens.Login.route) }
                )
            }
        }
    }
}