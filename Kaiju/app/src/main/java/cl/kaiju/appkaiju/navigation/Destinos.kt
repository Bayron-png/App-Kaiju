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
    val enBarra: Boolean = true // false = no sale en barra inferior
)
object Destinos {
    // Todos los destinos que la barra de navegacion inferior ofrece,
    // solo algunos se muestran para ciertos roles
    val todos = listOf(
        // INVENTARIO
        DestinoNavegacion(
            Rutas.Inventario.ruta, "Inventario", Icons.Default.Inventory,
            setOf(RolUsuario.ADMINISTRADOR, RolUsuario.ENCARGADO_INVENTARIO, RolUsuario.VENDEDOR)
        ),
        // ALERTAS
        DestinoNavegacion(
            Rutas.Alertas.ruta, "Alertas", Icons.Default.Notifications,
            setOf(RolUsuario.ADMINISTRADOR, RolUsuario.ENCARGADO_INVENTARIO)
        ),
        // REGISTRAR VENTA
        DestinoNavegacion(
            Rutas.RegistrarVenta.ruta, "Registrar Venta", Icons.Default.ShoppingCart,
            setOf(RolUsuario.VENDEDOR),
            enBarra = false //No aparece en la barra inferior (esta en un boton flotante)
        )
    )

    // Devuelve los destinos que deben mostrarse en la barra inferior
    // según el rol del usuario.
    // Solo incluye destinos visibles en la barra y autorizados para ese rol.
    fun paraRol(rol: RolUsuario) =
        todos.filter { it.enBarra && rol in it.roles }

    // Comprueba si un rol tiene permiso para acceder a una ruta.
    // No comprueba si la ruta aparece en la barra inferior.
    fun puedeEntrar(rol: RolUsuario, ruta: String) =
        todos.any{ it.ruta == ruta && rol in it.roles }

    // Determina la pantalla inicial según los permisos del rol.
    // Si el rol puede acceder al inventario, comienza allí.
    // En caso contrario, comienza en el catálogo.
    fun rutaInicio(rol: RolUsuario): String =
        if (puedeEntrar(rol, Rutas.Inventario.ruta)) Rutas.Inventario.ruta
        else Rutas.Catalogo.ruta
}