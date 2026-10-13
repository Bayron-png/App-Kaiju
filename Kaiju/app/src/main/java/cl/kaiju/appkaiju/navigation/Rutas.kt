package cl.kaiju.appkaiju.navigation

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Catalogo : Rutas("Inventario")
    object FormularioProducto : Rutas("formulario_producto")
    object DetalleProducto : Rutas("detalle_producto/{sku}") {
        fun crearRuta(sku: String) = "detalle_producto/$sku"
    }
    object Movimiento : Rutas("movimiento")
    object Alertas : Rutas("alertas")
}