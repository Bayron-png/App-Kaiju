package cl.kaiju.appkaiju.ui.screen.catalogo

import android.annotation.SuppressLint
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import cl.kaiju.appkaiju.ui.theme.Header
import cl.kaiju.appkaiju.ui.theme.TextoHeader


// Aceptacion de uso de API experimental, la API puede cambiar
// no tiene garantia de estabilidad
@OptIn(ExperimentalMaterial3Api::class)

// @SuppressLint = suprime una advertencia especifica del analizador estatico de Android
// "UnusedMaterial3ScaffoldPaddingParameter" = La advertencia que se silencia
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")


@Composable

fun PantallaCatalogo(
    // Accion que recibira desde la pantalla
    onVolver: () -> Unit
) {
    // "Esqueleto" que permite organizar elementos
    Scaffold(

        // Barra superior "Nuestro Catalogo"
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kaiju - Nuestro catálogo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Header,
                    titleContentColor = TextoHeader
                )
            )
        },
        // Barra inferior contiene botones de navegacion
        bottomBar = {
            // Permite navegar por medio de botones
            NavigationBar{

                // Boton "Volver"
                NavigationBarItem(
                    selected = false,
                    onClick = { onVolver() }, //
                    icon = {
                        Icon(Icons.Default.ArrowBackIosNew,
                            contentDescription = "Volver")
                    },
                    label = { Text("Volver")}
                )

                // Boton "Menú"
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Home,
                        contentDescription = "Menú")
                           },
                    label = { Text("Menú") }
                )
            } // fin NavigationBar
        } // fin bottomBar
    ) { }
} // fin PantallaCatologo

