package cl.kaiju.appkaiju.model

data class Producto(
    val sku: String,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val cantidadDisponible: Int,
    val stockMinimo: Int
) {

    val estadoStock: String
        get() = when {
            cantidadDisponible <= 0 -> "Producto agotado"
            cantidadDisponible <= stockMinimo -> "Producto con pocas unidades"
            else -> "Producto disponible"
        }
}
