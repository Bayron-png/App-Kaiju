package cl.kaiju.appkaiju.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.model.Producto
import cl.kaiju.appkaiju.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PantallaInventario(
    inventarioViewModel: InventarioViewModel = viewModel(),
    onCerrarSesion: () -> Unit = {},
    onIrAFormulario: () -> Unit = {},
    onIrADetalle: (String) -> Unit = {},
    onIrAAlertas: () -> Unit = {}
) {
    val productosState by inventarioViewModel.productos.collectAsState()

    val productosDePrueba = if (productosState.isNotEmpty()) {
        productosState
    } else {
        listOf(
            Producto(sku = "SKU-101", nombre = "Polera Roja Kaiju", categoria = "Ropa", precio = 15000.0, cantidadDisponible = 18, stock = 20, stockMinimo = 5),
            Producto(sku = "SKU-102", nombre = "Polera Azul Kaiju", categoria = "Ropa", precio = 16000.0, cantidadDisponible = 3, stock = 20, stockMinimo = 5)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kaiju - Catálogo de Inventario") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { onIrAFormulario() }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Ya estás aquí */ },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = "Catálogo") },
                    label = { Text("Catálogo") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { onIrAAlertas() },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Alertas") },
                    label = { Text("Alertas") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onCerrarSesion() },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Salir") },
                    label = { Text("Salir") }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productosDePrueba) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onIrADetalle(producto.sku) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(text = "SKU: ${producto.sku} | Disponibles: ${producto.cantidadDisponible}")
                        Text(text = "Estado: ${producto.estadoStock}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}