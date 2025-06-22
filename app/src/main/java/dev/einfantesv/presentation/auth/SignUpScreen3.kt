package dev.einfantesv.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dev.einfantesv.R
import dev.einfantesv.core.navigation.Screens
import dev.einfantesv.firebase.FirebaseAuthManager.registerUser
import dev.einfantesv.models.TempUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpScreen3(navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Variables del LoginScreen
        var passText by remember { mutableStateOf("") }
        var verifyPassText by remember { mutableStateOf("") }
        val isKeyboardVisible = WindowInsets.isImeVisible


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp, start = 1.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Button(
                    onClick = { navController.navigate(Screens.SignUp2.route) },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF),
                        contentColor = Color.Black,
                    ),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
                        .size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }

            }
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
                    value = verifyPassText,
                    onValueChange = { verifyPassText = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    label = { Text("Confirmar Password", color = Color(0xFF00C853)) },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
            // Separador
            Spacer(modifier = Modifier.height(5.dp))

            //Poner alerta de contraseñas no coincidentes
            if (!isKeyboardVisible && passText != verifyPassText) {
                Text(
                    text = "Las contraseñas no coinciden",
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }


            //Button Iniciar Sesión
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = registerUser(
                            nombre = TempUserData.nombre,
                            apellido = TempUserData.apellido,
                            email = TempUserData.email,
                            password = TempUserData.password
                        )

                        if (result.isSuccess) {
                            if (TempUserData.vendedor) {
                                navController.navigate("signUpVendedor")
                            } else {
                                navController.navigate(Screens.Login.route) {
                                    popUpTo(0)
                                }
                            }
                        }

                    }
                }
                ,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C853),
                    contentColor = Color.White
                ),
                enabled = passText == verifyPassText && passText.isNotBlank() && verifyPassText.isNotBlank(),
            ) {
                TempUserData.password = passText

                Text("Crear Cuenta",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }
    }
}