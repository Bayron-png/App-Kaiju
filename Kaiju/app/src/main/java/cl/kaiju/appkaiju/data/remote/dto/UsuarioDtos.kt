package cl.kaiju.appkaiju.data.remote.dto

import com.google.gson.annotations.SerializedName

// Estas clases son el JSON que envía y recibe el backend de Kaiju.

data class LoginRequest(val correo: String, val password: String)

data class UsuarioDto(
    val id: Int,
    val nombre: String,
    val correo: String,
    @SerializedName("rol_id") val rolId: Int,
    val rol: String
)

data class LoginResponse(
    val mensaje: String?,
    val token: String,
    val usuario: UsuarioDto
)

// El backend responde { mensaje, usuario } al crear, actualizar y eliminar.
data class UsuarioRespuesta(
    val mensaje: String?,
    val usuario: UsuarioDto
)

data class CrearUsuarioRequest(
    val nombre: String,
    val correo: String,
    val password: String,
    @SerializedName("rol_id") val rolId: Int
)

// Los campos null no se envían (Gson los omite), así que solo se actualiza lo que se cambia.
data class ActualizarUsuarioRequest(
    val nombre: String? = null,
    val correo: String? = null,
    @SerializedName("rol_id") val rolId: Int? = null,
    val password: String? = null
)

data class RolDto(val id: Int, val nombre: String)
