package cl.kaiju.appkaiju.viewmodel.login

import android.util.Patterns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.kaiju.appkaiju.model.login.EstadoLogin
import cl.kaiju.appkaiju.model.login.SesionActual
import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.model.usuario.Usuario
import cl.kaiju.appkaiju.repository.login.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(
    private val usuarioRepository: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoLogin())
    val estado: StateFlow<EstadoLogin> = _estado.asStateFlow()

    fun ingresarComoInvitado() {
        val invitado = Usuario(
            nombre = "Invitado",
            correo = "invitado@kaiju.cl",
            rol = RolUsuario.INVITADO
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

    // Consulta al backend. Si el login es correcto llama a onExito (ya con el rol guardado).
    fun iniciarSesion(onExito: () -> Unit) {
        // Valida el formato, si falla, no consulta al backend
        if (!validarFormularioLogin() || _estado.value.cargando) return

        val correo = _estado.value.correo
        val contrasena = _estado.value.contrasena
        _estado.value = _estado.value.copy(cargando = true, errorCredenciales = null)

        viewModelScope.launch {
            try {
                val sesion = usuarioRepository.autenticar(correo, contrasena)
                SesionActual.iniciarSesion(sesion.usuario, sesion.token)
                _estado.value = _estado.value.copy(
                    usuarioAutenticado = sesion.usuario,
                    cargando = false,
                    errorCorreo = null,
                    errorContrasena = null,
                    errorCredenciales = null
                )
                onExito()
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                // 401: un solo error generico, sin decir cual de los dos datos estaba mal
                val mensaje = if (e.code() == 401) {
                    "Correo o contraseña incorrectos"
                } else {
                    "Error del servidor (${e.code()}). Inténtalo nuevamente"
                }
                _estado.value = _estado.value.copy(cargando = false, errorCredenciales = mensaje)
            } catch (e: IOException) {
                _estado.value = _estado.value.copy(
                    cargando = false,
                    errorCredenciales = "No se pudo conectar con el servidor"
                )
            } catch (e: Exception) {
                _estado.value = _estado.value.copy(
                    cargando = false,
                    errorCredenciales = "Ocurrió un error inesperado"
                )
            }
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