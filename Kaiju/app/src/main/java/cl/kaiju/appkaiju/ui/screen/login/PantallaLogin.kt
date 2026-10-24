package cl.kaiju.appkaiju.ui.screen.login

import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import cl.kaiju.appkaiju.R
import androidx.compose.foundation.layout.size
import cl.kaiju.appkaiju.ui.theme.Primario
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.kaiju.appkaiju.ui.theme.Secundario
import cl.kaiju.appkaiju.viewmodel.login.LoginViewModel

@Composable

fun PantallaLogin(
    loginViewModel: LoginViewModel = viewModel(),
    onLoginExitoso: () -> Unit = {},
    onIngresarComoInvitado: () -> Unit = {}
) {
    // Observa el estado del ViewModel, si cambia la pantalla se redibuja sola
    val estado by loginViewModel.estado.collectAsState()

    // Permite controlar el foco:
    // mover el cursor entre campos o cerrar el teclado
    val focusManager = LocalFocusManager.current

    // Funcion que usan el boton "Ingresar" y el enter del teclado
    // para no duplicar logica
    val intentarLogin = {
        focusManager.clearFocus() // quita el foco y cierra el teclado
        if (loginViewModel.iniciarSesion()) { // true = credenciales correctas
            onLoginExitoso()                  // navega a la siguiente pantalla
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icono_kaiju),
            contentDescription = "Logo de Kaiju Store",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 12.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Kaiju Store",
            style = MaterialTheme.typography.headlineLarge,
            color = Primario,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = estado.correo,
            onValueChange = { loginViewModel.onCorreoChange(it) },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },

            // Rojo si hay error de formato o error de credenciales
            isError = estado.errorCorreo != null || estado.errorCredenciales != null,

            // Texto bajo el campo cuando ocurre el error propio del correo
            supportingText = { estado.errorCorreo?.let { Text(it) } },

            // El enter no hace salto de linea
            // El campo es de una sola linea
            singleLine = true,

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, // teclado con @
                imeAction = ImeAction.Next         // boton del teclado dice "siguiente"
            ),
            keyboardActions = KeyboardActions(
                // Al presionar "siguiente, el cursor baja al campo de contraseña
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.contrasena,
            onValueChange = { loginViewModel.onContrasenaChange(it) },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },

            // Muestra puntos en vez de letras
            visualTransformation = PasswordVisualTransformation(),

            isError = estado.errorContrasena != null || estado.errorCredenciales != null,

            // Muestra el error propio de la contraseña o de credenciales
            supportingText = {
                (estado.errorContrasena ?: estado.errorCredenciales)?.let { Text(it) }
            },

            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done // El boton del teclado dice "listo"
            ),
            keyboardActions = KeyboardActions(
                // Al presionar "listo" se intenta iniciar sesion, igual que con el boton
                onDone = { intentarLogin() }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { intentarLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primario
            )
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(12.dp)) // <-- Espacio

        TextButton(
            onClick = {
                loginViewModel.ingresarComoInvitado()
                onIngresarComoInvitado()
            }
        ) {
            Text(
                text = "Continuar como Invitado",
                color = Secundario
            )
        }

    }
}