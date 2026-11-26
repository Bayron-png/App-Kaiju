package cl.kaiju.appkaiju.util

object ValidadorProducto {

    fun validarFormulario(
        sku: String,
        nombre: String,
        categoria: String,
        precio: String,
        stock: String,
        stockMinimo: String
    ): String? {
        val skuLimpio = sku.trim()
        val precioDouble = precio.toDoubleOrNull()
        val stockInt = stock.toIntOrNull()
        val stockMinimoInt = stockMinimo.toIntOrNull()

        return when {
            skuLimpio.isBlank() || skuLimpio == "SKU-" ->
                "Ingresa un SKU válido."
            nombre.isBlank() ->
                "Ingresa el nombre del producto."
            categoria.isBlank() ->
                "Ingresa la categoría."
            precioDouble == null || !precioDouble.isFinite() || precioDouble < 0 ->
                "Ingresa un precio válido."
            stockInt == null || stockInt < 0 ->
                "Ingresa un stock inicial válido."
            stockMinimoInt == null || stockMinimoInt < 0 ->
                "Ingresa un stock mínimo válido."
            else -> null
        }
    }
}