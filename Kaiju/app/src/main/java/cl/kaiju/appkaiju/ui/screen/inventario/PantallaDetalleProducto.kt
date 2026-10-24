
package cl.kaiju.appkaiju.ui.screen.inventario

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    val movimientosFlow = remember(skuProducto) {
        inventarioViewModel.obtenerMovimientosPorProducto(skuProducto.orEmpty())
    }

    val movimientos by movimientosFlow.collectAsState(initial = emptyList())

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
                        text = "Historial de movimientos",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (movimientos.isEmpty()) {
                        Text("Este producto todavía no tiene movimientos registrados.")
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = movimientos,
                                key = { it.id }
                            ) { movimiento ->
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        val signo = when (movimiento.tipo) {
                                            "Entrada" -> "+"
                                            "Salida" -> "-"
                                            else -> ""
                                        }

                                        Text(
                                            text = "${movimiento.tipo}: $signo${movimiento.cantidad} unidades",
                                            style = MaterialTheme.typography.titleSmall
                                        )

                                        Text("Motivo: ${movimiento.motivo}")

                                        Text(
                                            text = SimpleDateFormat(
                                                "dd/MM/yyyy HH:mm",
                                                Locale.getDefault()
                                            ).format(Date(movimiento.fecha)),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
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
