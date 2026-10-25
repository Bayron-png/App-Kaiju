package cl.kaiju.appkaiju.ui.components.navegacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.navigation.Destinos

@Composable
fun BarraNavegacionRol(
    rol: RolUsuario,
    rutaActual: String,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    NavigationBar{
        Destinos.paraRol(rol).forEach { destino ->
            NavigationBarItem(
                selected = destino.ruta == rutaActual,
                onClick = { onNavegar(destino.ruta) },
                icon = {
                    Icon(
                        destino.icono,
                        contentDescription = destino.etiqueta
                        )
                },
                label = { Text (destino.etiqueta) }
            )
        }

        NavigationBarItem(
            selected = false,
            onClick = onCerrarSesion,
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Salir"
                )
            },
            label = { Text("Salir") }
        )
    }
}