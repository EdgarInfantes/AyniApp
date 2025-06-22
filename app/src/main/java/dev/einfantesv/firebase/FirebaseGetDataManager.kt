package dev.einfantesv.firebase

import com.google.firebase.firestore.FirebaseFirestore
import dev.einfantesv.models.ProductoFirebase
import dev.einfantesv.models.TempUserData.vendedor
import dev.einfantesv.models.VendedorFirebase
import kotlinx.coroutines.tasks.await

object FirebaseGetDataManager {

    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Obtiene la recomendación del día desde la colección "Recomendacion".
     * Espera que haya al menos un documento con campos: nombre, precio, imagen (url).
     */
    fun getRecomendacionDelDia(onComplete: (ProductoFirebase?) -> Unit) {
        firestore.collection("Recomendacion")
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val producto = snapshot.documents.firstOrNull()?.toObject(ProductoFirebase::class.java)
                onComplete(producto)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun getOrdenesDeVendedor(
        vendedorId: String,
        onComplete: (List<Map<String, Any>>) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        firestore.collection("Ordenes")
            .whereEqualTo("vendedorId", vendedorId)
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val ordenes = snapshot.documents.mapNotNull { doc ->
                    doc.data?.toMutableMap()?.apply { put("id", doc.id) }
                }
                onComplete(ordenes)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }



    fun getVendedores(onComplete: (List<VendedorFirebase>) -> Unit) {
        firestore.collection("Vendedores")
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.documents.mapNotNull { doc ->
                    val vendedor: VendedorFirebase? = doc.toObject(VendedorFirebase::class.java)
                    vendedor?.copy(uid = doc.id)
                }
                onComplete(lista)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }

    fun getProductosPorVendedor(vendedorId: String, onComplete: (List<ProductoFirebase>) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("Productos")
            .whereEqualTo("uidVendedor", vendedorId)
            .get()
            .addOnSuccessListener { snapshot ->
                val productos = snapshot.documents.mapNotNull {
                    it.toObject(ProductoFirebase::class.java)
                }
                onComplete(productos)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }


    suspend fun isUserVendor(uid: String): Boolean {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("Vendedores")
            .document(uid)
            .get()
            .await()
        return snapshot.exists()
    }

}
