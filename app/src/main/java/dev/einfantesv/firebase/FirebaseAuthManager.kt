package dev.einfantesv.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import dev.einfantesv.models.TempUserData

object FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Registro del usuario (comprador o vendedor)
    suspend fun registerUser(
        nombre: String,
        apellido: String,
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val authResult = auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID inválido"))

            val nombreCompleto = "$nombre $apellido".trim()

            val userData = hashMapOf(
                "nombre" to nombreCompleto,
                "email" to email,
                "rol" to if (TempUserData.vendedor) "vendedor" else "comprador"
            )

            val collection = if (TempUserData.vendedor) "Vendedores" else "Compradores"

            firestore.collection(collection)
                .document(uid)
                .set(userData)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Registro extendido del vendedor (datos adicionales)
    suspend fun registerAsVendor(
        nombre: String,
        apellido: String,
        email: String,
        descripcion: String,
        contacto: String,
        horarios: Map<String, Pair<String, String>>,
        logoUrl: String = ""
    ): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("No hay usuario autenticado"))

            val uid = currentUser.uid

            val nombreCompleto = "$nombre $apellido".trim()

            val vendedorData = mapOf(



                "uid" to uid,
                "email" to email,
                "nombre" to nombreCompleto, // <- nos aseguramos de guardar bien el nombre
                "contacto" to contacto,
                "imagen" to logoUrl,
                "horarios" to horarios.mapValues {
                    mapOf("inicio" to it.value.first, "fin" to it.value.second)
                },
                "calificacion" to "0.0",
                "tiempoEntrega" to "20-30 min",
                "horarioDisponible" to "Ver horarios",
                "descripcion" to descripcion
            )

            firestore.collection("Vendedores")
                .document(uid)
                .set(vendedorData)
                .await()

            Log.d("RegistroVendedor", "Nombre del vendedor: ${nombreCompleto.trim()}")

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Inicio de sesión
    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Verifica si ya hay sesión activa
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
    fun signOut() {
        auth.signOut()
    }

    // Obtiene el rol del usuario autenticado ("vendedor" o "comprador")
    suspend fun getUserRole(): String? {
        val uid = auth.currentUser?.uid ?: return null

        // Primero verifica en la colección de Vendedores
        val vendedorSnapshot = firestore.collection("Vendedores").document(uid).get().await()
        if (vendedorSnapshot.exists()) return "vendedor"

        // Luego verifica en la colección de Usuarios
        val usuarioSnapshot = firestore.collection("Compradores").document(uid).get().await()
        if (usuarioSnapshot.exists()) return "comprador"

        return null // No se encontró en ninguna colección
    }

    fun getCurrentUser() = auth.currentUser

}
