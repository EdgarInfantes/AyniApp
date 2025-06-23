package dev.einfantesv.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
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
    fun getVendedoresQueVendenProducto(
        nombreProducto: String,
        onComplete: (List<VendedorFirebase>) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("Productos")
            .whereEqualTo("nombre", nombreProducto)
            .get()
            .addOnSuccessListener { productosSnapshot ->
                val productos = productosSnapshot.documents
                val vendedoresIds = productos.mapNotNull { it.getString("uidVendedor") }.toSet()

                if (vendedoresIds.isEmpty()) {
                    onComplete(emptyList())
                    return@addOnSuccessListener
                }

                if (vendedoresIds.size > 10) {
                    onError(Exception("Demasiados vendedores para buscar con whereIn (máx. 10)"))
                    return@addOnSuccessListener
                }

                firestore.collection("Vendedores")
                    .whereIn("uid", vendedoresIds.toList())
                    .get()
                    .addOnSuccessListener { vendedoresSnapshot ->
                        val vendedores = vendedoresSnapshot.documents.mapNotNull { doc ->
                            doc.toObject(VendedorFirebase::class.java)
                        }
                        onComplete(vendedores)
                    }
                    .addOnFailureListener { onError(it) }

            }
            .addOnFailureListener { onError(it) }
    }


    fun getOrdenesDeComprador(
        compradorId: String,
        onComplete: (List<Map<String, Any>>) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        FirebaseFirestore.getInstance()
            .collection("Pedidos")
            .whereEqualTo("uidComprador", compradorId)
            .orderBy("fechaHora", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d("CompradorOrdenes", "uidComprador: $compradorId")
                Log.d("CompradorOrdenes", "Documentos recuperados: ${snapshot.size()}")
                val ordenes = snapshot.documents.mapNotNull { doc ->
                    doc.data?.toMutableMap()?.apply { put("id", doc.id) }
                }
                onComplete(ordenes)
            }
            .addOnFailureListener { exception ->
                Log.e("CompradorOrdenes", "Error al obtener órdenes: ${exception.message}", exception)
                onError(exception)
            }
    }


    fun getOrdenesDeVendedor(
        vendedorId: String,
        onComplete: (List<Map<String, Any>>) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        firestore.collection("Pedidos")
            .whereEqualTo("uidVendedor", vendedorId)
            .orderBy("fechaHora", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d("CompradorOrdenes", "uidComprador: ${vendedorId}")
                Log.d("CompradorOrdenes", "Documentos recuperados: ${snapshot.size()}")
                val ordenes = snapshot.documents.mapNotNull { doc ->
                    doc.data?.toMutableMap()?.apply { put("id", doc.id) }
                }
                onComplete(ordenes)
            }
            .addOnFailureListener { exception ->
                Log.e("VendedorOrdenes", "Error al obtener órdenes: ${exception.message}", exception)
                onError(exception)
            }
    }

    fun getNombre(coleccion: String, uid: String, onComplete: (String?) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection(coleccion)
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val nombre = doc.getString("nombre") ?: doc.getString("displayName")
                onComplete(nombre)
            }
            .addOnFailureListener {
                onComplete(null)
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
    fun getProductoPorId(productoId: String, onComplete: (ProductoFirebase?) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("Productos")
            .document(productoId)
            .get()
            .addOnSuccessListener { snapshot ->
                val producto = snapshot.toObject(ProductoFirebase::class.java)
                onComplete(producto)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun realizarPedido(
        producto: ProductoFirebase,
        cantidad: Int,
        nota: String,
        onComplete: (Boolean) -> Unit
    ) {
        val uidComprador = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val pedido = mapOf(
            "productoId" to producto.id,
            "productoNombre" to producto.nombre,
            "uidVendedor" to producto.uidVendedor,
            "uidComprador" to uidComprador,
            "cantidad" to cantidad,
            "nota" to nota,
            "precioTotal" to (producto.precio?.toDoubleOrNull()?.times(cantidad) ?: 0.0),
            "fechaHora" to com.google.firebase.Timestamp.now(),
            "estado" to "Pendiente"
        )

        FirebaseFirestore.getInstance()
            .collection("Pedidos")
            .add(pedido)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun marcarComoEntregado(pedidoId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("Pedidos")
            .document(pedidoId)
            .update("estado", "Entregado")
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e) }
    }


}
