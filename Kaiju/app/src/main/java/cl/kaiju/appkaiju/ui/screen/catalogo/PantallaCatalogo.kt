
package cl.kaiju.appkaiju.ui.screen.catalogo

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.kaiju.appkaiju.R
import cl.kaiju.appkaiju.ui.theme.Header
import cl.kaiju.appkaiju.ui.theme.TextoHeader
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantallaCatalogo(
    inventarioViewModel: InventarioViewModel,
    onVolver: () -> Unit,
    onIrADetalle: (String) -> Unit
) {
    // Obtener los productos desde el ViewModel.
    val productosState by inventarioViewModel.productos.collectAsState()

    Scaffold(
        // Barra superior del catálogo.
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(
                                id = R.drawable.icono_kaiju
                            ),
                            contentDescription = "Logo de Kaiju Store",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 10.dp)
                        )

                        Text(
                            text = "Kaiju - Nuestro catálogo",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Header,
                    titleContentColor = TextoHeader
                )
            )
        },

        // Barra inferior.
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { onVolver() },
                    icon = {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Salir"
                        )
                    },
                    label = {
                        Text("Salir")
                    }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Catálogo"
                        )
                    },
                    label = {
                        Text("Catálogo")
                    }
                )
            }
        }
    ) { paddingValues ->

        // Lista de productos disponibles.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = productosState,
                key = { producto -> producto.sku }
            ) { producto ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onIrADetalle(producto.sku)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Categoría: ${producto.categoria}"
                        )

                        Text(
                            text = "Precio: $${producto.precio.toInt()}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
