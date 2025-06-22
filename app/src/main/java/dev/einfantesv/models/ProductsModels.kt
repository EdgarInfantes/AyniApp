package dev.einfantesv.models

data class ProductoFirebase(
    val id: String = "",
    val uidVendedor: String = "",
    val nombre: String = "",
    val precio: String = "",
    val imagen: String = ""
)

data class VendedorFirebase(
    val uid: String = "",
    val nombre: String = "",
    val imagen: String = "",
    val contacto: String = "",
    val calificacion: String = "",
    val tiempoEntrega: String = "",
    val horarioDisponible: String = ""
)


data class CompradorFirebase(
    val nombre: String = "",
    val imagen: String = ""
)

object TempUserData {
    var nombre: String = ""
    var apellido: String = ""
    var email: String = ""
    var password: String = ""
    var vendedor: Boolean = false
}
