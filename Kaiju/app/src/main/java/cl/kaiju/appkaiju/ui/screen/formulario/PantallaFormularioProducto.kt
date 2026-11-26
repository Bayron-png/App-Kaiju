package cl.kaiju.appkaiju.ui.screen.formulario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel
import kotlinx.coroutines.launch

@Composable
fun PantallaFormularioProducto(
    inventarioViewModel: InventarioViewModel = viewModel(),
    onVolver: () -> Unit,
    onGuardarExitoso: () -> Unit
) {
    val estadoFormulario by inventarioViewModel.estadoFormulario.collectAsState()
    val scope = rememberCoroutineScope()

    PantallaFormularioProductoContent(
        estado = estadoFormulario,
        onSkuChange = inventarioViewModel::onSkuChange,
        onNombreChange = inventarioViewModel::onNombreChange,
        onCategoriaChange = inventarioViewModel::onCategoriaChange,
        onPrecioChange = inventarioViewModel::onPrecioChange,
        onStockChange = inventarioViewModel::onStockChange,
        onStockMinimoChange = inventarioViewModel::onStockMinimoChange,
        onVolver = onVolver,
        onGuardarProducto = {
            scope.launch {
                val exito = inventarioViewModel.guardarProducto()
                if (exito) {
                    onGuardarExitoso()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioProductoContent(
    estado: InventarioViewModel.EstadoFormularioProducto,
    onSkuChange: (String) -> Unit,
    onNombreChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onStockMinimoChange: (String) -> Unit,
    onVolver: () -> Unit,
    onGuardarProducto: () -> Unit
) {
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

            MainFormFields(
                estado = estado,
                onSkuChange = onSkuChange,
                onNombreChange = onNombreChange,
                onCategoriaChange = onCategoriaChange,
                onPrecioChange = onPrecioChange,
                onStockChange = onStockChange,
                onStockMinimoChange = onStockMinimoChange,
                onGuardarProducto = onGuardarProducto
            )
        }
    }
}

@Composable
private fun MainFormFields(
    estado: InventarioViewModel.EstadoFormularioProducto,
    onSkuChange: (String) -> Unit,
    onNombreChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onStockMinimoChange: (String) -> Unit,
    onGuardarProducto: () -> Unit
) {
    OutlinedTextField(
        value = estado.sku,
        onValueChange = onSkuChange,
        label = { Text("SKU") },
        isError = estado.errorSku != null,
        supportingText = { estado.errorSku?.let { Text(it) } },
        trailingIcon = {
            if (estado.errorSku != null) {
                Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = estado.nombre,
        onValueChange = onNombreChange,
        label = { Text("Nombre del Producto") },
        isError = estado.errorNombre != null,
        supportingText = { estado.errorNombre?.let { Text(it) } },
        trailingIcon = {
            if (estado.errorNombre != null) {
                Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = estado.categoria,
        onValueChange = onCategoriaChange,
        label = { Text("Categoría") },
        isError = estado.errorCategoria != null,
        supportingText = { estado.errorCategoria?.let { Text(it) } },
        trailingIcon = {
            if (estado.errorCategoria != null) {
                Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = estado.precio,
        onValueChange = onPrecioChange,
        label = { Text("Precio") },
        isError = estado.errorPrecio != null,
        supportingText = { estado.errorPrecio?.let { Text(it) } },
        trailingIcon = {
            if (estado.errorPrecio != null) {
                Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = estado.stock,
        onValueChange = onStockChange,
        label = { Text("Stock Inicial") },
        isError = estado.errorStock != null,
        supportingText = { estado.errorStock?.let { Text(it) } },
        trailingIcon = {
            if (estado.errorStock != null) {
                Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    OutlinedTextField(
        value = estado.stockMinimo,
        onValueChange = onStockMinimoChange,
        label = { Text("Stock Mínimo") },
        isError = estado.errorStockMinimo != null,
        supportingText = {
            if (estado.errorStockMinimo != null) {
                Text(estado.errorStockMinimo)
            } else {
                Text("Generará una alerta cuando el stock llegue a este nivel o menos.")
            }
        },
        trailingIcon = {
            if (estado.errorStockMinimo != null) {
                Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onGuardarProducto,
        modifier = Modifier.fillMaxWidth(),
        enabled = !estado.guardando
    ) {
        if (estado.guardando) {
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
