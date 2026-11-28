package cl.kaiju.appkaiju.model.usuario

enum class RolUsuario(val etiqueta: String) {
    ADMINISTRADOR("Administrador"),
    VENDEDOR("Personal de ventas"),
    ENCARGADO_INVENTARIO("Encargado de inventario"),
    INVITADO("Invitado");

    // De momento solo admin puede agregar un producto
    val puedeModificarInventario: Boolean
        get() = this == ADMINISTRADOR

    companion object {
        // Convierte el nombre del rol que entrega el backend al enum de la app
        fun desdeBackend(nombre: String): RolUsuario? = when (nombre) {
            "Administrador" -> ADMINISTRADOR
            "Encargado de inventario" -> ENCARGADO_INVENTARIO
            "Vendedor" -> VENDEDOR
            else -> null
        }
    }
}