package cl.kaiju.appkaiju.model.login

data class Usuario(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: RolUsuario
)