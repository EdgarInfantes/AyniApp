package dev.einfantesv

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dev.einfantesv.core.navigation.Screens

@Composable
fun LoginScreen(navController: NavHostController){

    //Variables del LoginScreen
    var userText by remember { mutableStateOf("") }
    var passText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .imePadding(),
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

        /*
        //Fila de Usuario
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            //Box para el texto
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .height(55.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                //Text: Usuario
                Text(
                    text = "Usuario",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontWeight = FontWeight.Bold
                )
            }

            // Separador
            Spacer(modifier = Modifier.width(1.dp))

            //Box para el Texfield
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                //TextField: Usuario
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = userText,
                    onValueChange = { userText = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text("usuario@dominio.com") }
                )
            }

        }

        // Separador
        Spacer(modifier = Modifier.height(10.dp))

        //Fila de Contraseña
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            //Box para el texto
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .height(55.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                //Text: Password
                Text(
                    text = "Password",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontWeight = FontWeight.Bold
                )
            }

            // Separador
            Spacer(modifier = Modifier.width(1.dp))

            //Box para el Texfield
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = 6.dp, horizontal = 6.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Red,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                //TextField: Password
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = passText,
                    onValueChange = { passText = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )
            }

        }
        */
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
                value = userText,
                onValueChange = { userText = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                label = { Text("Usuario", color = Color(0xFF00C853))},
                placeholder = { Text("usuario@dominio.com", color = Color(0xFFB5B0AD))},
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
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = passText,
                onValueChange = { passText = it },
                colors = TextFieldDefaults.colors(

                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent

                ),
                label = { Text("Password", color = Color(0xFF00C853)) },
                visualTransformation = PasswordVisualTransformation()
            )
        }

        // Separador
        Spacer(modifier = Modifier.height(5.dp))

        //Button Iniciar Sesión
        Button(
            onClick = { navController.navigate(Screens.Home.route) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00C853),
                contentColor = Color.White
            ),
            enabled = userText.isNotBlank() && passText.isNotBlank()
        ) {
            Text("Ingresar",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFFFFFF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

        }

        //Fila Crear Cuenta
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 10.dp),
        )
        {
            Text(
                text = "O puedes"
            )
            // Separador
            Spacer(modifier = Modifier.width(5.dp))

            //Texto Clickeale para mandar al SignUpScreen
            Text(
                text = "Registrarte",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF00C853),
                fontSize = 16.sp,
                modifier = Modifier.clickable { navController.navigate(Screens.SignUp.route) }
            )

        }

        //Fila Resetear Contraseña
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 10.dp)
        )
        {
            Text(
                text = "¿Olvidaste tu contraseña?"
            )
            // Separador
            Spacer(modifier = Modifier.width(5.dp))

            //Texto Clickeale para mandar al SignUpScreen
            Text(
                text = "Recuperar Contraseña",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF00C853),
                fontSize = 16.sp,
                modifier = Modifier.clickable { navController.navigate(Screens.ResetPassword.route) }
            )

        }
    }
}