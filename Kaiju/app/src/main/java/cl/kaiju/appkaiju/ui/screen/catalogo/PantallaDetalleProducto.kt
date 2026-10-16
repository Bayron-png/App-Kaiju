package cl.kaiju.appkaiju.ui.screen.catalogo

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PantallaDetalleProducto(
    skuProducto: String?,
    inventarioViewModel: InventarioViewModel = viewModel(),
    onVolver: () -> Unit,
    onIrAMovimiento: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto: $skuProducto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("SKU: $skuProducto", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Estado: Producto disponible")
                    Text("Precio actual: $15.000")
                    Text("Stock disponible: 18 unidades")
                }
            }

            Text(
                "Historial de Movimientos recientes (RF-04):",
                style = MaterialTheme.typography.titleMedium
            )
            Text("• Ingreso de stock (+10) - 10/10/2026")
            Text("• Salida por venta (-2) - 11/10/2026")

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (skuProducto != null) {
                        inventarioViewModel.seleccionarSku(skuProducto)
                    }
                    onIrAMovimiento()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Nuevo Movimiento")
            }
        }
    }
}