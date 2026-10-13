package cl.kaiju.appkaiju.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PantallaMovimiento(
    onVolver: () -> Unit,
    onMovimientoRegistrado: () -> Unit
) {
    var tipoMovimiento by remember { mutableStateOf("Entrada") }
    var cantidad by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Movimiento (RF-06/08)") },
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
            Text("Tipo de Movimiento:", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { tipoMovimiento = "Entrada" }, colors = ButtonDefaults.buttonColors(containerColor = if(tipoMovimiento=="Entrada") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)) { Text("Entrada") }
                Button(onClick = { tipoMovimiento = "Salida" }, colors = ButtonDefaults.buttonColors(containerColor = if(tipoMovimiento=="Salida") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)) { Text("Salida") }
                Button(onClick = { tipoMovimiento = "Ajuste" }, colors = ButtonDefaults.buttonColors(containerColor = if(tipoMovimiento=="Ajuste") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)) { Text("Ajuste") }
            }

            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo / Observación") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onMovimientoRegistrado,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Movimiento")
            }
        }
    }
}