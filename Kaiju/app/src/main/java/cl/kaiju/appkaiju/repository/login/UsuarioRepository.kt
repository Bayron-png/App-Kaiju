package cl.kaiju.appkaiju.repository.login

import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.model.usuario.Usuario

class UsuarioRepository {

    private val usuarios = listOf(
        Usuario("Administrador", "admin@kaiju.cl", "admin123", RolUsuario.ADMINISTRADOR),
        Usuario("Ventas", "ventas@kaiju.cl", "ventas123", RolUsuario.VENDEDOR),
        Usuario("Encargado de Bodega", "stock@kaiju.cl", "stock123", RolUsuario.ENCARGADO_INVENTARIO)
    )

    fun autenticar(correo: String, contrasena: String): Usuario? {
        return usuarios.find {
            it.correo.equals(correo, ignoreCase = true) && it.contrasena == contrasena
        }
    }

    fun existeCorreo(correo: String): Boolean {
        return usuarios.any { it.correo.equals(correo, ignoreCase = true) }
    }
}