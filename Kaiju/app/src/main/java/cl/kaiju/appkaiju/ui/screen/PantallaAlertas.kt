package cl.kaiju.appkaiju.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAlertas(
    inventarioViewModel: InventarioViewModel = viewModel(),
    onVolver: () -> Unit = {},
    onSeleccionarAlerta: (String) -> Unit = {}
) {
    val productosState by inventarioViewModel.productos.collectAsState()

    val productosCriticos = remember(productosState) {
        productosState.filter { it.cantidadDisponible <= it.stockMinimo }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alertas de Stock") },
                navigationIcon = {
                    IconButton(onClick = { onVolver() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (productosCriticos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
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