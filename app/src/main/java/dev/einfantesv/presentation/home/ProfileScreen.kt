package dev.einfantesv.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import dev.einfantesv.R
import dev.einfantesv.UserSessionViewModel
import dev.einfantesv.firebase.FirebaseGetDataManager
import dev.einfantesv.util.Headers

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel
) {
    val context = LocalContext.current
    val profileImageUrl by userSessionViewModel.profileImageUrl.collectAsState()
    val userData by userSessionViewModel.userData.collectAsState()

    var showImageOptions by remember { mutableStateOf(false) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { profileImageUri = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Headers("Administrar mi cuenta")
        Spacer(modifier = Modifier.height(24.dp))

        // Imagen de perfil
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFF00C853), CircleShape)
                .clickable { showImageOptions = true },
            contentAlignment = Alignment.Center
        ) {
            if (!profileImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Avatar por defecto",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre del usuario
        Text(
            text = userData?.nombre ?: "Usuario",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileOptionButton("Cambiar nombre") { /* Lógica aquí */ }
        ProfileOptionButton("Cambiar contraseña") { /* Lógica aquí */ }

        var isVendor by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                isVendor = FirebaseGetDataManager.isUserVendor(uid)
            }
        }

        // Mostrar opciones distintas
        if (isVendor) {
            ProfileOptionButton("Administrar negocio") {
                navController.navigate("adminVendedor")

            }
        } else {
            ProfileOptionButton("Registrarme como vendedor") {
                navController.navigate("signUpVendedor")
            }
        }


        ProfileOptionButton("Cerrar sesión", R.drawable.baseline_logout_24, Color.Red) {
            userSessionViewModel.signOut()
            navController.navigate("login") {
                popUpTo(0)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileOptionButton(
    text: String,
    iconResId: Int? = null,
    textColor: Color = Color.Black,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(70.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = textColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        color = textColor
                    )
                )
            }
            iconResId?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
