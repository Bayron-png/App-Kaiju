package cl.kaiju.appkaiju.model
data class Movimiento(
    val id: Long = System.nanoTime(),
    val sku: String,
    val nombreProducto: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val stockAnterior: Int,
    val stockResultante: Int,
    val motivo: String = "",
    val fecha: Long = System.currentTimeMillis()
)   {
    val descripcion: String
        get() = when (tipo) {
            TipoMovimiento.AJUSTE ->
                "[Ajuste] SKU: $sku - Stock $stockAnterior → $stockResultante"
            else ->
                "[${tipo.etiqueta}] SKU: $sku - Cantidad: $cantidad (Stock actual: $stockResultante)"
        }
}