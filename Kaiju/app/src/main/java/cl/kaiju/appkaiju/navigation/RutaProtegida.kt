package cl.kaiju.appkaiju.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cl.kaiju.appkaiju.model.usuario.RolUsuario

// Permite mostrar el contenido solo si el usuario tiene un rol
// válido y está autorizado para acceder a la ruta solicitada.
// Si no cumple ambas condiciones, ejecuta la acción de acceso denegado.
@Composable
fun RutaProtegida(
    rol: RolUsuario?,
    ruta: String,
    onDenegado: () -> Unit,
    contenido: @Composable (RolUsuario) -> Unit
) {
    if (rol != null && Destinos.puedeEntrar(rol,ruta)) {
        contenido(rol)
    } else {
        LaunchedEffect(rol) { onDenegado()}
    }
}