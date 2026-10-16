package cl.kaiju.appkaiju.model.producto
data class Producto(
    val sku: String, // StockKeepingUnit: Código del producto
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val cantidadDisponible: Int,
    val stock: Int,
    val stockMinimo: Int
) {

    val estadoStock: String
        get() = when {
            cantidadDisponible <= 0 -> "Producto agotado"
            cantidadDisponible <= stockMinimo -> "Producto con pocas unidades"
            else -> "Producto disponible"
        }
}

