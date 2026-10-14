package cl.kaiju.appkaiju.model
enum class TipoMovimiento(val etiqueta: String) {
    ENTRADA("entrada"),
    SALIDA_VENTA("salida_venta"),
    SALIDA_CONSUMO("salida_consumo"),
    AJUSTE("ajuste")
}
