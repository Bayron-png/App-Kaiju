package cl.kaiju.appkaiju.ui.screen.alertas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.navigation.Rutas
import cl.kaiju.appkaiju.ui.components.navegacion.PlantillaPantallaRol
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAlertas(
    rol: RolUsuario,
    inventarioViewModel: InventarioViewModel = viewModel(),
    onNavegar: (String) -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    onSeleccionarAlerta: (String) -> Unit = {}
) {
    val productosState by inventarioViewModel.productos.collectAsState()

    val productosCriticos = remember(productosState) {
        productosState.filter { it.cantidadDisponible <= it.stockMinimo }
    }

    PlantillaPantallaRol(
        titulo = "Alertas de stock",
        rol = rol,
        rutaActual = Rutas.Alertas.ruta,
        onNavegar = onNavegar,
        onCerrarSesion = onCerrarSesion
    ) { padding ->
        if (productosCriticos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay alertas de stock por el momento.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productosCriticos) { producto ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onSeleccionarAlerta(producto.sku) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (producto.cantidadDisponible == 0)
                                MaterialTheme.colorScheme.errorContainer
                            else
                                MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.padding(end = 12.dp))
                            Column {
                                Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(text = "SKU: ${producto.sku} | Disponibles: ${producto.cantidadDisponible} (Mínimo: ${producto.stockMinimo})")
                                Text(text = "Estado: ${producto.estadoStock}", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}