package cl.kaiju.appkaiju.navigation

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Inventario : Rutas("inventario")
}