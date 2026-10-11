package cl.kaiju.appkaiju.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Inventory
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.model.Producto
import cl.kaiju.appkaiju.ui.components.ListaProductos
import cl.kaiju.appkaiju.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaInventario(
    inventarioViewModel: InventarioViewModel = viewModel(),
    onCerrarSesion: () -> Unit = {}
) {
    val productosState by inventarioViewModel.productos.collectAsState()

    val productosDePrueba = if (productosState.isNotEmpty()) {
        productosState
    } else {
        listOf(
            Producto("SKU-101", "Polera Roja Kaiju", "Ropa", 15000.0, 18, 5),
            Producto("SKU-102", "Polera Azul Kaiju", "Ropa", 16000.0, 3, 5),
            Producto("SKU-103", "Polera Blanca Kaiju", "Ropa", 17000.0, 0, 10),
            Producto("SKU-104", "Polera Verde Kaiju", "Ropa", 18000.0, 12, 4),
            Producto("SKU-105", "Polera Morada Kaiju", "Ropa", 19000.0, 2, 8)
        )
    }

    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kaiju - Control de Inventario") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Inventory,
                        contentDescription = "Logo Kaiju"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = "Inventario") },
                    label = { Text("Inventario") }
                )
                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        onCerrarSesion()
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión") },
                    label = { Text("Salir") }
                )
            }
        }
    ) { innerPadding ->
        ListaProductos(
            productos = productosDePrueba,
            paddingValues = innerPadding
        )
    }
}