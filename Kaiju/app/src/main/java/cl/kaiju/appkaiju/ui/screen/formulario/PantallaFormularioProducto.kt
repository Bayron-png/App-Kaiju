package cl.kaiju.appkaiju.ui.screen.formulario

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.model.producto.Producto
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioProducto(
    inventarioViewModel: InventarioViewModel = viewModel(),
    onVolver: () -> Unit,
    onGuardarExitoso: () -> Unit
) {
    var sku by remember { mutableStateOf("SKU-") }
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("1") }

    var errorFormulario by remember { mutableStateOf<String?>(null) }
    var guardando by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

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
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Gestionar un nuevo producto",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = sku,
                onValueChange = {
                    sku = it
                    errorFormulario = null
                },
                label = { Text("SKU") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock Inicial") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = stockMinimo,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.all { it.isDigit() }) {
                        stockMinimo = nuevoValor
                    }
                },
                label = { Text("Stock Mínimo") },
                supportingText = {
                    Text("Generará una alerta cuando el stock llegue a este nivel o menos.")
                },
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
                    if (guardando) {
                        return@Button
                    }

                    val skuLimpio = sku.trim()
                    val precioDouble = precio.toDoubleOrNull()
                    val stockInt = stock.toIntOrNull()
                    val stockMinimoInt = stockMinimo.toIntOrNull()

                    errorFormulario = when {
                        skuLimpio.isBlank() || skuLimpio == "SKU-" ->
                            "Ingresa un SKU válido."

                        nombre.isBlank() ->
                            "Ingresa el nombre del producto."

                        categoria.isBlank() ->
                            "Ingresa la categoría."

                        precioDouble == null || !precioDouble.isFinite() ||
                                precioDouble < 0 ->
                            "Ingresa un precio válido."

                        stockInt == null || stockInt < 0 ->
                            "Ingresa un stock inicial válido."

                        stockMinimoInt == null || stockMinimoInt < 0 ->
                            "Ingresa un stock mínimo válido."

                        else -> null
                    }

                    if (errorFormulario == null) {
                        val nuevoProducto = Producto(
                            sku = skuLimpio,
                            nombre = nombre.trim(),
                            categoria = categoria.trim(),
                            precio = precioDouble!!,
                            cantidadDisponible = stockInt!!,
                            stock = stockInt,
                            stockMinimo = stockMinimoInt!!
                        )

                        scope.launch {
                            guardando = true

                            try {
                                val creado =
                                    inventarioViewModel.agregarProducto(nuevoProducto)

                                if (creado) {
                                    onGuardarExitoso()
                                } else {
                                    errorFormulario =
                                        "Este SKU ya existe. Introduce uno diferente."
                                }
                            } catch (e: Exception) {
                                errorFormulario =
                                    "No se pudo guardar el producto. Inténtalo nuevamente."
                            } finally {
                                guardando = false
                            }
                        }
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