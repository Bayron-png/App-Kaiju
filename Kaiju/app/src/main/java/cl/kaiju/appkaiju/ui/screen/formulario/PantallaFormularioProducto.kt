package cl.kaiju.appkaiju.ui.screen.formulario

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
import cl.kaiju.appkaiju.model.producto.Producto
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel
import kotlinx.coroutines.launch

@Composable
fun PantallaFormularioProducto(
    inventarioViewModel: InventarioViewModel = viewModel(),
    onVolver: () -> Unit,
    onGuardarExitoso: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var guardando by remember { mutableStateOf(false) }
    var errorFormulario by remember { mutableStateOf<String?>(null) }

    PantallaFormularioProductoContent(
        guardando = guardando,
        errorFormulario = errorFormulario,
        onVolver = onVolver,
        onGuardarProducto = { nuevoProducto ->
            scope.launch {
                guardando = true
                try {
                    inventarioViewModel.agregarProducto(nuevoProducto)
                    onGuardarExitoso()
                } catch (e: Exception) {
                    errorFormulario = "No se pudo guardar el producto. Inténtalo nuevamente."
                } finally {
                    guardando = false
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioProductoContent(
    guardando: Boolean,
    errorFormulario: String?,
    onVolver: () -> Unit,
    onGuardarProducto: (Producto) -> Unit
) {
    var sku by remember { mutableStateOf("SKU-") }
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("1") }

    var errorLocal by remember { mutableStateOf<String?>(null) }
    val errorMostrar = errorFormulario ?: errorLocal

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Producto") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Gestionar un nuevo producto",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = sku,
                onValueChange = {
                    sku = it
                    errorLocal = null
                },
                label = { Text("SKU") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    errorLocal = null
                },
                label = { Text("Nombre del Producto") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = {
                    categoria = it
                    errorLocal = null
                },
                label = { Text("Categoría") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = precio,
                onValueChange = {
                    precio = it
                    errorLocal = null
                },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = stock,
                onValueChange = {
                    stock = it
                    errorLocal = null
                },
                label = { Text("Stock Inicial") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = stockMinimo,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.all { it.isDigit() }) {
                        stockMinimo = nuevoValor
                        errorLocal = null
                    }
                },
                label = { Text("Stock Mínimo") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                supportingText = {
                    Text("Generará una alerta cuando el stock llegue a este nivel o menos.")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            errorMostrar?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (guardando) return@Button

                    val skuLimpio = sku.trim()
                    val precioDouble = precio.toDoubleOrNull()
                    val stockInt = stock.toIntOrNull()
                    val stockMinimoInt = stockMinimo.toIntOrNull()

                    val mensajeError = when {
                        skuLimpio.isBlank() || skuLimpio == "SKU-" -> "Ingresa un SKU válido."
                        nombre.isBlank() -> "Ingresa el nombre del producto."
                        categoria.isBlank() -> "Ingresa la categoría."
                        precioDouble == null || !precioDouble.isFinite() || precioDouble < 0 -> "Ingresa un precio válido."
                        stockInt == null || stockInt < 0 -> "Ingresa un stock inicial válido."
                        stockMinimoInt == null || stockMinimoInt < 0 -> "Ingresa un stock mínimo válido."
                        else -> null
                    }

                    if (mensajeError != null) {
                        errorLocal = mensajeError
                    } else if (precioDouble != null && stockInt != null && stockMinimoInt != null) {
                        val nuevoProducto = Producto(
                            sku = skuLimpio,
                            nombre = nombre.trim(),
                            categoria = categoria.trim(),
                            precio = precioDouble,
                            cantidadDisponible = stockInt,
                            stock = stockInt,
                            stockMinimo = stockMinimoInt
                        )
                        onGuardarProducto(nuevoProducto)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !guardando
            ) {
                if (guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardando...")
                } else {
                    Text("Guardar Producto")
                }
            }
        }
    }
}