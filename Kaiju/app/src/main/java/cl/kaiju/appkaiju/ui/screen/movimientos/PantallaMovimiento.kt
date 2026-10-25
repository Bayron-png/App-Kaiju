package cl.kaiju.appkaiju.ui.screen.movimientos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMovimiento(
    inventarioViewModel: InventarioViewModel = viewModel(),
    onVolver: () -> Unit,
    onMovimientoRegistrado: () -> Unit
) {
    var tipoMovimiento by remember { mutableStateOf("Entrada") }
    var cantidad by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var errorMovimiento by remember { mutableStateOf<String?>(null) }

    val skuActual = inventarioViewModel.skuSeleccionado

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (skuActual.isNotEmpty()) {
                            "Movimiento ($skuActual)"
                        } else {
                            "Registrar Movimiento"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            Text(
                text = "Tipo de Movimiento:",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        tipoMovimiento = "Entrada"
                        errorMovimiento = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tipoMovimiento == "Entrada") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )
                ) {
                    Text("Entrada")
                }

                Button(
                    onClick = {
                        tipoMovimiento = "Salida"
                        errorMovimiento = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tipoMovimiento == "Salida") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )
                ) {
                    Text("Salida")
                }

                Button(
                    onClick = {
                        tipoMovimiento = "Ajuste"
                        errorMovimiento = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tipoMovimiento == "Ajuste") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )
                ) {
                    Text("Ajuste")
                }
            }

            OutlinedTextField(
                value = cantidad,
                onValueChange = {
                    cantidad = it
                    errorMovimiento = null
                },
                label = { Text("Cantidad") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = motivo,
                onValueChange = {
                    motivo = it
                    errorMovimiento = null
                },
                label = { Text("Motivo / Observación") },
                modifier = Modifier.fillMaxWidth()
            )

            errorMovimiento?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val cantidadInt = cantidad.toIntOrNull()

                    errorMovimiento = when {
                        skuActual.isBlank() ->
                            "No hay un producto seleccionado."

                        cantidadInt == null || cantidadInt <= 0 ->
                            "Ingresa una cantidad válida mayor que cero."

                        motivo.isBlank() ->
                            "Ingresa el motivo del movimiento."

                        tipoMovimiento == "Salida" &&
                                cantidadInt > (
                                inventarioViewModel.productos.value
                                    .firstOrNull { it.sku == skuActual }
                                    ?.cantidadDisponible ?: 0
                                ) ->
                            "No hay suficiente stock para realizar esta salida."

                        else -> null
                    }

                    if (errorMovimiento == null) {
                        inventarioViewModel.registrarMovimiento(
                            sku = skuActual,
                            tipo = tipoMovimiento,
                            cantidad = cantidadInt!!,
                            motivo = motivo.trim()
                        )

                        onMovimientoRegistrado()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Movimiento")
            }
        }
    }
}