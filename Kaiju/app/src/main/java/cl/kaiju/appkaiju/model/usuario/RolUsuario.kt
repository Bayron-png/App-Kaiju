package cl.kaiju.appkaiju.model.usuario

enum class RolUsuario(val etiqueta: String) {
    ADMINISTRADOR("Administrador"),
    VENDEDOR("Personal de ventas"),
    ENCARGADO_INVENTARIO("Encargado de inventario"),
    INVITADO("Invitado")
}