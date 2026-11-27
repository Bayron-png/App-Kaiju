package cl.kaiju.appkaiju.ui.screen.Usuarios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.kaiju.appkaiju.model.usuario.RolUsuario

data class UsuarioMemoria(
    val id: Int,
    val nombre: String,
    val correo: String,
    val perfil: RolUsuario
)

private fun RolUsuario.etiquetaVisible(): String = when (this) {
    RolUsuario.ADMINISTRADOR -> "Administrador"
    RolUsuario.ENCARGADO_INVENTARIO -> "Encargado de inventario"
    RolUsuario.VENDEDOR -> "Vendedor"
    RolUsuario.INVITADO -> TODO()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaGestionUsuarios(
    onVolver: () -> Unit
) {
    val usuarios = remember {
        mutableStateListOf(
            UsuarioMemoria(1, "Admin Kaiju", "admin@kaiju.cl", RolUsuario.ADMINISTRADOR),
            UsuarioMemoria(2, "Camila Rojas", "bodega@kaiju.cl", RolUsuario.ENCARGADO_INVENTARIO),
            UsuarioMemoria(3, "Matías Soto", "ventas@kaiju.cl", RolUsuario.VENDEDOR)
        )
    }

    var mostrarFormulario by remember { mutableStateOf(false) }
    var usuarioAEditar by remember { mutableStateOf<UsuarioMemoria?>(null) }
    var usuarioAEliminar by remember { mutableStateOf<UsuarioMemoria?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    usuarioAEditar = null
                    mostrarFormulario = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear usuario")
            }
        }
    ) { padding ->
        if (usuarios.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay usuarios registrados.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(usuarios, key = { it.id }) { usuario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = usuario.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = usuario.correo,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text(usuario.perfil.etiquetaVisible()) }
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        usuarioAEditar = usuario
                                        mostrarFormulario = true
                                    }
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(
                                    onClick = { usuarioAEliminar = usuario }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarFormulario) {
        DialogoUsuarioMemoria(
            usuarioInicial = usuarioAEditar,
            onDismiss = { mostrarFormulario = false },
            onGuardar = { nombre, correo, perfil ->
                val actual = usuarioAEditar
                if (actual == null) {
                    val nuevoId = (usuarios.maxOfOrNull { it.id } ?: 0) + 1
                    usuarios.add(
                        UsuarioMemoria(
                            id = nuevoId,
                            nombre = nombre.trim(),
                            correo = correo.trim(),
                            perfil = perfil
                        )
                    )
                } else {
                    val index = usuarios.indexOfFirst { it.id == actual.id }
                    if (index != -1) {
                        usuarios[index] = actual.copy(
                            nombre = nombre.trim(),
                            correo = correo.trim(),
                            perfil = perfil
                        )
                    }
                }
                mostrarFormulario = false
            }
        )
    }

    usuarioAEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = { usuarioAEliminar = null },
            title = { Text("Eliminar usuario") },
            text = { Text("¿Eliminar a ${usuario.nombre} (${usuario.perfil.etiquetaVisible()})?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        usuarios.removeAll { it.id == usuario.id }
                        usuarioAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { usuarioAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoUsuarioMemoria(
    usuarioInicial: UsuarioMemoria?,
    onDismiss: () -> Unit,
    onGuardar: (String, String, RolUsuario) -> Unit
) {
    val perfiles = RolUsuario.entries
    var nombre by remember { mutableStateOf(usuarioInicial?.nombre ?: "") }
    var correo by remember { mutableStateOf(usuarioInicial?.correo ?: "") }
    var perfilSeleccionado by remember { mutableStateOf(usuarioInicial?.perfil ?: RolUsuario.VENDEDOR) }
    var expandido by remember { mutableStateOf(false) }
    var errorValidacion by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (usuarioInicial == null) "Nuevo Usuario" else "Editar Usuario")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        errorValidacion = null
                    },
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        errorValidacion = null
                    },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { expandido = !expandido }
                ) {
                    OutlinedTextField(
                        value = perfilSeleccionado.etiquetaVisible(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Perfil") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandido,
                        onDismissRequest = { expandido = false }
                    ) {
                        perfiles.forEach { perfil ->
                            DropdownMenuItem(
                                text = { Text(perfil.etiquetaVisible()) },
                                onClick = {
                                    perfilSeleccionado = perfil
                                    expandido = false
                                }
                            )
                        }
                    }
                }

                errorValidacion?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    when {
                        nombre.isBlank() -> errorValidacion = "Ingresa el nombre del usuario."
                        correo.isBlank() || !correo.contains("@") -> errorValidacion = "Ingresa un correo válido."
                        else -> onGuardar(nombre, correo, perfilSeleccionado)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}