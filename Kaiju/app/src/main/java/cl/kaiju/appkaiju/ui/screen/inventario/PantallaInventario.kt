package cl.kaiju.appkaiju.ui.screen.inventario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.model.producto.Producto
import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.navigation.Rutas
import cl.kaiju.appkaiju.ui.components.navegacion.PlantillaPantallaRol
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PantallaInventario(
    rol: RolUsuario,
    inventarioViewModel: InventarioViewModel = viewModel(),
    onNavegar: (String) -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    onIrAFormulario: () -> Unit = {},
    onIrADetalle: (String) -> Unit = {},
    onIrARegistrarVenta: () -> Unit = {}
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

    PlantillaPantallaRol(
        titulo = "Kaiju - Inventario",
        rol = rol,
        rutaActual = Rutas.Inventario.ruta,
        onNavegar = onNavegar,
        onCerrarSesion = onCerrarSesion,
        floatingActionButton = {
            if (rol.puedeModificarInventario) {
                FloatingActionButton( onClick = {onIrAFormulario()} ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
                }
            } else if (rol == RolUsuario.VENDEDOR) {
                FloatingActionButton(onClick = { onIrARegistrarVenta()} ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Registrar venta")
                }
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