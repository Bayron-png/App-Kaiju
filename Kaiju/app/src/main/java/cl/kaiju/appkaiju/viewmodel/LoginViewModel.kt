package cl.kaiju.appkaiju.viewmodel

import androidx.lifecycle.ViewModel
import cl.kaiju.appkaiju.model.EstadoLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _estado = MutableStateFlow(EstadoLogin())
    val estado: StateFlow<EstadoLogin> = _estado.asStateFlow()

    fun onCorreoChange(nuevoCorreo: String) {
        _estado.value = _estado.value.copy(
            correo = nuevoCorreo,
            errorCorreo = null
        )
    }

    fun onContrasenaChange(nuevaContrasena: String) {
        _estado.value = _estado.value.copy(
            contrasena = nuevaContrasena,
            errorContrasena = null
        )
    }

    fun validarFormulario(): Boolean {
        val actual = _estado.value
        var esValido = true
        var errCorreo: String? = null
        var errContrasena: String? = null

        if (actual.correo.isBlank()) {
            errCorreo = "El correo es obligatorio"
            esValido = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(actual.correo).matches()) {
            errCorreo = "Ingrese un correo válido (ej: usuario@kaiju.com)"
            esValido = false
        }

        if (actual.contrasena.isBlank()) {
            errContrasena = "La contraseña no puede estar vacía"
            esValido = false
        } else if (actual.contrasena.length < 6) {
            errContrasena = "Debe tener al menos 6 caracteres"
            esValido = false
        }

        _estado.value = actual.copy(
            errorCorreo = errCorreo,
            errorContrasena = errContrasena
        )

        return esValido
    }
}