package cl.kaiju.appkaiju.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import cl.kaiju.appkaiju.model.usuario.RolUsuario


data class DestinoNavegacion(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector,
    val roles: Set<RolUsuario>,
    val enBarra: Boolean = true
)
object Destinos {
    val todos = listOf(
        DestinoNavegacion(
            Rutas.Inventario.ruta, "Inventario", Icons.Default.Inventory,
            setOf(RolUsuario.ADMINISTRADOR, RolUsuario.ENCARGADO_INVENTARIO, RolUsuario.VENDEDOR)
        ),
        DestinoNavegacion(
            Rutas.Alertas.ruta, "Alertas", Icons.Default.Notifications,
            setOf(RolUsuario.ADMINISTRADOR, RolUsuario.ENCARGADO_INVENTARIO)
        ),
        DestinoNavegacion(
            Rutas.RegistrarVenta.ruta, "Registrar Venta", Icons.Default.ShoppingCart,
            setOf(RolUsuario.VENDEDOR),
            enBarra = false
        )
    )
    fun paraRol(rol: RolUsuario) =
        todos.filter { it.enBarra && rol in it.roles }

    fun puedeEntrar(rol: RolUsuario, ruta: String) =
        todos.any{ it.ruta == ruta && rol in it.roles }

    fun rutaInicio(rol: RolUsuario): String =
        if (puedeEntrar(rol, Rutas.Inventario.ruta)) Rutas.Inventario.ruta
        else Rutas.Catalogo.ruta
}