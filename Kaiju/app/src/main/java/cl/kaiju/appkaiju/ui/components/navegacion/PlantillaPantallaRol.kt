package cl.kaiju.appkaiju.ui.components.navegacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.kaiju.appkaiju.R
import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.ui.theme.Header
import cl.kaiju.appkaiju.ui.theme.TextoHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Componente reutilizable
fun PlantillaPantallaRol(
    titulo: String,
    rol: RolUsuario,
    rutaActual: String,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit,
    floatingActionButton: @Composable () -> Unit = {}, // opcional
    contenido: @Composable (PaddingValues) -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.icono_kaiju),
                            contentDescription = "Logo de Kaiju Store",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 10.dp)
                        )

                        Text(
                            text = titulo,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp,
                            color = TextoHeader
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Header,
                    titleContentColor = TextoHeader
                )
            )
        },
        bottomBar = {
            BarraNavegacionRol(
                rol, rutaActual, onNavegar, onCerrarSesion
            )
        },
        floatingActionButton = floatingActionButton,
        content = contenido
    )

}