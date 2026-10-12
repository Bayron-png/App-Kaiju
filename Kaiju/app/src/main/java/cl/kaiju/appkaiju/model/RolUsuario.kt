package cl.kaiju.appkaiju.model

enum class RolUsuario(val etiqueta: String) {
    ADMINISTRADOR("Administrador"),
    VENTAS("Ventas"),
    STOCK("Encargado de Stock"),
    CLIENTE("Invitado")
}