package cl.kaiju.appkaiju.navigation

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")

    // Ruta de catalogo de productos
    object Catalogo : Rutas("catalogo")
    object Inventario : Rutas("Inventario")
    object FormularioProducto : Rutas("formulario_producto")
    object DetalleProducto : Rutas("detalle_producto/{sku}/{esInvitado}") {
        fun crearRuta(sku: String, esInvitado: Boolean) =
            "detalle_producto/$sku/$esInvitado"
    }
    object Movimiento : Rutas("movimiento")
    object Alertas : Rutas("alertas")
}