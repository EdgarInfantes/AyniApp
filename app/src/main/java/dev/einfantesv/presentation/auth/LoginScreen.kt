package dev.einfantesv.presentation.auth

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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dev.einfantesv.R
import dev.einfantesv.UserSessionViewModel
import dev.einfantesv.core.navigation.Screens
import dev.einfantesv.firebase.FirebaseAuthManager
import dev.einfantesv.models.TempUserData
import dev.einfantesv.util.ActionButton
import dev.einfantesv.util.AnimatedSnackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController){

    //Variables del LoginScreen
    var userText by remember { mutableStateOf("") }
    var passText by remember { mutableStateOf("") }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarColor by remember { mutableStateOf(Color.Green) }
    val viewModel: UserSessionViewModel = viewModel()

    var mensaje by remember { mutableStateOf("") }
    var snackbarVisible by remember { mutableStateOf(false) }

    // Oculta el snackbar automáticamente después de 2 segundos
    LaunchedEffect(snackbarVisible) {
        if (snackbarVisible) {
            kotlinx.coroutines.delay(2000)
            snackbarVisible = false
        }
    }

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
        ActionButton(
            label = "Iniciar sesión",
            onClick = {
                val emailError = userText.isBlank()
                val passwordError = passText.isBlank()

                if (!emailError && !passwordError) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = FirebaseAuthManager.loginUser(userText, passText)
                        val rol = FirebaseAuthManager.getUserRole()

                        if (result.isSuccess) {
                            if (rol == "comprador") {
                                // Cargar los datos del usuario al iniciar sesión
                                viewModel.loadUserData()
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                    launchSingleTop = true
                                }

                            } else if (rol == "vendedor") {
                                navController.navigate("homeVendedor") {
                                    popUpTo(0)
                                }
                            }

                            snackbarMessage = "Bienvenido"
                            snackbarColor = Color(0xFF00C853)


                        }
                        else {
                            snackbarMessage = "Datos incorrectos"
                            snackbarColor = Color(0xFFF44336)
                        }
                        snackbarVisible = true
                    }
                } else {
                    snackbarVisible = true
                    snackbarMessage = "Complete los campos"
                    snackbarColor = Color(0xFFF44336)
                }
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "O puedes registrarte como ")

            Text(
                text = "comprador",
                color = Color(0xFF00C853),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                modifier = Modifier.clickable {
                    navController.navigate(Screens.SignUp.route)
                }
            )

            Text(text = " o ")

            Text(
                text = "vendedor",

                color = Color(0xFF00C853),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                modifier = Modifier.clickable {
                    TempUserData.vendedor = true
                    navController.navigate(Screens.SignUp.route)
                }
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (snackbarVisible) {
                AnimatedSnackbar(
                    visible = snackbarVisible,
                    message = snackbarMessage, // <-- aquí estaba el problema
                    backgroundColor = snackbarColor,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .heightIn(min = 48.dp)
                )
            }
        }

    }
}