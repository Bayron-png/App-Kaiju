
package cl.kaiju.appkaiju.ui.screen.inventario

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    onIrAMovimiento: () -> Unit,
    esInvitado: Boolean = false
) {
    // Obtener los productos reales desde el ViewModel
    val productos by inventarioViewModel.productos.collectAsState()

    // Buscar el producto seleccionado mediante su SKU
    val producto = productos.firstOrNull {
        it.sku == skuProducto
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(producto?.nombre ?: "Detalle del producto")
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
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
            if (producto == null) {
                Text(
                    text = "Cargando producto o producto no encontrado.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        HorizontalDivider()


                        Text("SKU: ${producto.sku}")
                        Text("Categoría: ${producto.categoria}")
                        Text("Precio: $${producto.precio.toInt()}")
                        Text("Cantidad disponible: ${producto.cantidadDisponible} unidades")

                        if (!esInvitado) {
                            Text("Stock registrado: ${producto.stock} unidades")
                            Text("Stock mínimo: ${producto.stockMinimo} unidades")

                            Text(
                                text = "Estado: ${producto.estadoStock}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                if (!esInvitado) {
                    Text(
                        text = "Historial de movimientos recientes (RF-04):",
                        style = MaterialTheme.typography.titleMedium
                    )

                    // El historial real se debe conectar a los movimientos
                    // guardados en la base de datos.
                    Text(
                        "El historial de movimientos aún no está conectado."
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            inventarioViewModel.seleccionarSku(producto.sku)
                            onIrAMovimiento()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrar Nuevo Movimiento")
                    }
                }
            }
        }
    }
}
