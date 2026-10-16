package cl.kaiju.appkaiju.model
data class Producto(
    val sku: String,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val cantidadDisponible: Int,
    val stock: Int,
    val stockMinimo: Int
) {

    val estadoStock: EstadoStock
        get() = when {
            cantidadDisponible <= 0 -> EstadoStock.AGOTADO
            cantidadDisponible <= stockMinimo -> EstadoStock.BAJO
            else -> EstadoStock.DISPONIBLE
        }
}
