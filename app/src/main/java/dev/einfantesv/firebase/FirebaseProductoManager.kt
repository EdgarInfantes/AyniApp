package dev.einfantesv.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.einfantesv.models.ProductoFirebase
import kotlinx.coroutines.tasks.await
import java.util.UUID

object FirebaseProductoManager {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun agregarProducto(nombre: String, precio: String, imagenUrl: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))

            val producto = ProductoFirebase(
                id = UUID.randomUUID().toString(),
                uidVendedor = uid,
                nombre = nombre,
                precio = precio,
                imagen = imagenUrl
            )

            firestore.collection("Productos")
                .document(producto.id)
                .set(producto)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerMisProductos(): Result<List<ProductoFirebase>> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))

            val snapshot = firestore.collection("Productos")
                .whereEqualTo("uidVendedor", uid)
                .get()
                .await()

            val productos = snapshot.documents.mapNotNull { it.toObject(ProductoFirebase::class.java) }
            Result.success(productos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarProducto(producto: ProductoFirebase): Result<Unit> {
        return try {
            firestore.collection("Productos")
                .document(producto.id)
                .set(producto)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
