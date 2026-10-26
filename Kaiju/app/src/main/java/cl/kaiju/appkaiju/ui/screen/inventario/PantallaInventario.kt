package cl.kaiju.appkaiju.ui.screen.inventario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

    val busqueda by inventarioViewModel.busqueda.collectAsState()
    val productosFitrados by inventarioViewModel.productosFiltrados.collectAsState()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { inventarioViewModel.onBusquedaChange(it) },
                label = { Text("Buscar por nombre o SKU") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null)},
                trailingIcon = {
                    // aparece una x solo si hay texto
                    if (busqueda.isNotEmpty()){
                        IconButton(onClick = { inventarioViewModel.onBusquedaChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar busqueda")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer( modifier =  Modifier.height(12.dp))

            if (productosFitrados.isEmpty()){
                Text("No se encontraron productos")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productosFitrados) { producto ->
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
    }
}