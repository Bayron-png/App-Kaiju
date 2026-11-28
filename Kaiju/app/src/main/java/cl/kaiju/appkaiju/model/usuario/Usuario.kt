package cl.kaiju.appkaiju.model.usuario

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val rol: RolUsuario
)
