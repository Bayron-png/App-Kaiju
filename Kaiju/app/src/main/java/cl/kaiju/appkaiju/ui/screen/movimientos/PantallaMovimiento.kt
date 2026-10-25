
package cl.kaiju.appkaiju.ui.screen.movimientos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.compose.runtime.rememberCoroutineScope
import cl.kaiju.appkaiju.util.NotificacionStock
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val permisoNotificaciones = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        // El permiso se comprueba nuevamente cuando se necesita mostrar
        // una notificación. Si se deniega, el movimiento igualmente se registra.
    }

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
                        skuLimpio.isBlank() ->
                            "Ingresa un SKU válido."

                        productos.none { it.sku == skuLimpio } ->
                            "No existe un producto con el SKU ingresado."

                        cantidadInt == null || cantidadInt <= 0 ->
                            "Ingresa una cantidad mayor a 0."

                        else -> null
                    }

                    if (error != null) {
                        errorFormulario = error
                    } else {
                        scope.launch {
                            val productoAlerta =
                                inventarioViewModel.registrarMovimiento(
                                    sku = skuLimpio,
                                    esEntrada = esEntrada,
                                    cantidad = cantidadInt!!
                                )

                            if (productoAlerta != null) {
                                val permisoConcedido =
                                    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                                            ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.POST_NOTIFICATIONS
                                            ) == PackageManager.PERMISSION_GRANTED

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    !permisoConcedido
                                ) {
                                    permisoNotificaciones.launch(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                } else if (permisoConcedido) {
                                    NotificacionStock.mostrar(
                                        context,
                                        productoAlerta
                                    )
                                }
                            }

                            onMovimientoRegistrado()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Movimiento")
            }
        }
    }
}
