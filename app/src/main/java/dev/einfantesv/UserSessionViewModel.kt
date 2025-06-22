package dev.einfantesv

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.einfantesv.models.CompradorFirebase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserSessionViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _userUid = MutableStateFlow(auth.currentUser?.uid)
    val userUid: StateFlow<String?> = _userUid

    private val _userData = MutableStateFlow<CompradorFirebase?>(null)
    val userData: StateFlow<CompradorFirebase?> = _userData

    val profileImageUrl: StateFlow<String?> = userData
        .map { it?.imagen }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        observeUidChanges()
    }

    private fun observeUidChanges() {
        viewModelScope.launch {
            userUid.collect { uid ->
                if (uid != null) {
                    loadUserData(uid)
                } else {
                    _userData.value = null
                }
            }
        }
    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun getCurrentUserUid(): String? = auth.currentUser?.uid

    fun refreshUserData(uid: String? = getCurrentUserUid()) {
        val safeUid = uid ?: return

        firestore.collection("Usuario").document(safeUid).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    _userData.value = snapshot.toObject(CompradorFirebase::class.java)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("UserSessionViewModel", "Error al refrescar datos del usuario", exception)
            }
    }

    fun signOut() {
        auth.signOut()
        _userUid.value = null
        _userData.value = null
    }

    fun loadUserData(uid: String? = getCurrentUserUid()) {
        uid?.let {
            firestore.collection("Compradores").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(CompradorFirebase::class.java)
                        Log.d("UserSessionViewModel", "Usuario cargado: $user")
                        _userData.value = user
                    } else {
                        Log.w("UserSessionViewModel", "Documento no existe.")
                    }
                }
                .addOnFailureListener {
                    Log.e("UserSessionViewModel", "Error al cargar datos", it)
                    _userData.value = null
                }
        }
    }
}
