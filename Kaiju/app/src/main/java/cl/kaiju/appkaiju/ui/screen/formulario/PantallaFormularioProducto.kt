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

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PantallaFormularioProducto(
    inventarioViewModel: InventarioViewModel = viewModel(),
    onVolver: () -> Unit,
    onGuardarExitoso: () -> Unit
) {
    var sku by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario Producto (Admin)") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
            Text("Gestión exclusiva de administrador (RF-05)", style = MaterialTheme.typography.bodyMedium)

            OutlinedTextField(value = sku, onValueChange = { sku = it }, label = { Text("SKU") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del Producto") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock Inicial") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val stockInt = stock.toIntOrNull() ?: 10
                    val nuevoProducto = Producto(
                        sku = sku,
                        nombre = nombre,
                        categoria = categoria,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        cantidadDisponible = stockInt,
                        stock = stockInt,
                        stockMinimo = 3
                    )

                    inventarioViewModel.agregarProducto(nuevoProducto)
                    onGuardarExitoso()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto")
            }
        }
    }
}