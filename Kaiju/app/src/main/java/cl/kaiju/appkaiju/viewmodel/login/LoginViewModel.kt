package cl.kaiju.appkaiju.viewmodel.login

import android.util.Patterns

import androidx.lifecycle.ViewModel
import cl.kaiju.appkaiju.model.login.EstadoLogin
import cl.kaiju.appkaiju.model.login.SesionActual
import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.model.usuario.Usuario
import cl.kaiju.appkaiju.repository.login.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val usuarioRepository: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoLogin())
    val estado: StateFlow<EstadoLogin> = _estado.asStateFlow()

    fun ingresarComoInvitado() {
        val invitado = Usuario(
            "Invitado",
            "invitado@kaiju.cl",
            "",
            RolUsuario.INVITADO
        )

        SesionActual.iniciarSesion(invitado)

        _estado.value = _estado.value.copy(
            usuarioAutenticado = invitado
        )
    }

    fun onCorreoChange(nuevoCorreo: String) {

        val limpio = nuevoCorreo
            // elimina espacios, tabs y saltos de linea
            .filterNot { it.isWhitespace() }
            // corta a un maximo de 60 caracteres
            .take(60)

        _estado.value = _estado.value.copy(
            correo = limpio,
            // Al escribir, se quita el error de formato
            errorCorreo = null,
            // y el error de credenciales
            errorCredenciales = null
        )
    }

    fun onContrasenaChange(nuevaContrasena: String) {
        val limpio = nuevaContrasena
            // solo quita saltos de linea, no espacios
            .filterNot { it == '\n' || it == '\r' }
            // maximo de 40 caracteres
            .take(40)

        _estado.value = _estado.value.copy(
            contrasena = limpio,
            errorContrasena = null,
            errorCredenciales = null
        )
    }

    fun cerrarSesion() {
        SesionActual.cerrarSesion()
        _estado.value = EstadoLogin()
    }

    fun iniciarSesion(): Boolean {

        // Valida el formato, si falla, no consulta al repositorio
        if (!validarFormularioLogin()) {
            return false
        }

        val correo = _estado.value.correo
        val contrasena = _estado.value.contrasena

        // Devuelve el usuario si coinciden correo y contraseña o null si no
        val usuario = usuarioRepository.autenticar(correo, contrasena)

        return if (usuario != null) {
            SesionActual.iniciarSesion(usuario)

            // Exito: guarda el usuario (con su rol) y limpia todos los errores
            _estado.value = _estado.value.copy(
                usuarioAutenticado = usuario,
                errorCorreo = null,
                errorContrasena = null,
                errorCredenciales = null
            )
            true
        } else {
            // Fallo: un solo error generico que pinta ambos campos en rojo,
            // sin mostrar cual de los dos datos estaba mal
            _estado.value = _estado.value.copy(
                errorCredenciales = "Correo o contraseña incorrectos"
            )
            false
        }
    }

    private fun validarFormularioLogin(): Boolean {
        val actual = _estado.value
        var esValido = true
        var errCorreo: String? = null
        var errContrasena: String? = null

        // Correo: valida que no este vacio y tenga un formato valido
        if (actual.correo.isBlank()) {
            errCorreo = "Ingrese su correo"
            esValido = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(actual.correo).matches()) {
            errCorreo = "Ingrese un correo válido"
            esValido = false
        }

        // Contraseña: solo comprueba que no este vacio.
        // habia un largo minimo pero eso va en el registro, no aqui
        if (actual.contrasena.isBlank()) {
            errContrasena = "Ingrese su contraseña"
            esValido = false
        }

        // Guarda los errores en el estado para que la pantalla los muestre
        _estado.value = actual.copy(
            errorCorreo = errCorreo,
            errorContrasena = errContrasena
        )

        return esValido // true = el formulario esta bien
    }
}