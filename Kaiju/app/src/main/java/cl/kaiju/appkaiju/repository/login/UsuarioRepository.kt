package cl.kaiju.appkaiju.repository.login

import cl.kaiju.appkaiju.data.remote.KaijuApi
import cl.kaiju.appkaiju.data.remote.KaijuApiClient
import cl.kaiju.appkaiju.data.remote.dto.ActualizarUsuarioRequest
import cl.kaiju.appkaiju.data.remote.dto.CrearUsuarioRequest
import cl.kaiju.appkaiju.data.remote.dto.LoginRequest
import cl.kaiju.appkaiju.data.remote.dto.RolDto
import cl.kaiju.appkaiju.data.remote.dto.UsuarioDto
import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.model.usuario.Usuario

data class SesionIniciada(val usuario: Usuario, val token: String)

// Habla con el backend de Kaiju (ya no hay usuarios escritos en el código).
class UsuarioRepository(
    private val api: KaijuApi = KaijuApiClient.api
) {

    // Lanza HttpException (401 = credenciales inválidas) o IOException (sin conexión)
    suspend fun autenticar(correo: String, contrasena: String): SesionIniciada {
        val respuesta = api.login(LoginRequest(correo.trim(), contrasena))
        val rol = RolUsuario.desdeBackend(respuesta.usuario.rol)
            ?: throw IllegalStateException("Rol desconocido: ${respuesta.usuario.rol}")
        val usuario = Usuario(
            id = respuesta.usuario.id,
            nombre = respuesta.usuario.nombre,
            correo = respuesta.usuario.correo,
            rol = rol
        )
        return SesionIniciada(usuario, respuesta.token)
    }

    // --- CRUD de usuarios (solo Administrador) ---

    suspend fun listarUsuarios(): List<UsuarioDto> = api.listarUsuarios()

    suspend fun listarRoles(): List<RolDto> = api.listarRoles()

    suspend fun crearUsuario(
        nombre: String,
        correo: String,
        password: String,
        rolId: Int
    ): UsuarioDto =
        api.crearUsuario(CrearUsuarioRequest(nombre.trim(), correo.trim(), password, rolId)).usuario

    suspend fun actualizarUsuario(
        id: Int,
        nombre: String? = null,
        correo: String? = null,
        rolId: Int? = null,
        password: String? = null
    ): UsuarioDto =
        api.actualizarUsuario(id, ActualizarUsuarioRequest(nombre, correo, rolId, password)).usuario

    suspend fun eliminarUsuario(id: Int) {
        api.eliminarUsuario(id)
    }
}
