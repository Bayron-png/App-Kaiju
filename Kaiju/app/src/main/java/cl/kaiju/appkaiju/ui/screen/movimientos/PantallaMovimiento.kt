package cl.kaiju.appkaiju.ui.screen.movimientos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    val productos by inventarioViewModel.productos.collectAsState()
    val skuInicial = inventarioViewModel.skuSeleccionado

    var sku by remember { mutableStateOf(skuInicial) }
    var esEntrada by remember { mutableStateOf(true) }
    var cantidadText by remember { mutableStateOf("") }
    var errorFormulario by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Movimiento") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registro de Entrada o Salida de Stock",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = sku,
                onValueChange = {
                    sku = it
                    errorFormulario = null
                },
                label = { Text("SKU del Producto") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = esEntrada,
                    onClick = { esEntrada = true },
                    label = { Text("Entrada (+)") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = !esEntrada,
                    onClick = { esEntrada = false },
                    label = { Text("Salida (-)") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = cantidadText,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        cantidadText = it
                        errorFormulario = null
                    }
                },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            errorFormulario?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val skuLimpio = sku.trim()
                    val cantidadInt = cantidadText.toIntOrNull()

                    val error = when {
                        skuLimpio.isBlank() -> "Ingresa un SKU válido."
                        productos.none { it.sku == skuLimpio } -> "No existe un producto con el SKU ingresado."
                        cantidadInt == null || cantidadInt <= 0 -> "Ingresa una cantidad mayor a 0."
                        else -> null
                    }

                    if (error != null) {
                        errorFormulario = error
                    } else {
                        inventarioViewModel.registrarMovimiento(
                            sku = skuLimpio,
                            esEntrada = esEntrada,
                            cantidad = cantidadInt!!
                        )
                        onMovimientoRegistrado()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Movimiento")
            }
        }
    }
}
