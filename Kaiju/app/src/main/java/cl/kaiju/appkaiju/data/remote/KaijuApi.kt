package cl.kaiju.appkaiju.data.remote

import cl.kaiju.appkaiju.data.remote.dto.ActualizarUsuarioRequest
import cl.kaiju.appkaiju.data.remote.dto.CrearUsuarioRequest
import cl.kaiju.appkaiju.data.remote.dto.LoginRequest
import cl.kaiju.appkaiju.data.remote.dto.LoginResponse
import cl.kaiju.appkaiju.data.remote.dto.RolDto
import cl.kaiju.appkaiju.data.remote.dto.UsuarioDto
import cl.kaiju.appkaiju.data.remote.dto.UsuarioRespuesta
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Endpoints del backend de Kaiju. Las rutas son relativas a BASE_URL.
interface KaijuApi {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("usuarios")
    suspend fun listarUsuarios(): List<UsuarioDto>

    @POST("usuarios")
    suspend fun crearUsuario(@Body body: CrearUsuarioRequest): UsuarioRespuesta

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Body body: ActualizarUsuarioRequest
    ): UsuarioRespuesta

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): UsuarioRespuesta

    @GET("roles")
    suspend fun listarRoles(): List<RolDto>
}
