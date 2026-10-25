package cl.kaiju.appkaiju.model.login

import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.model.usuario.Usuario

object SesionActual {

    var usuario: Usuario? = null

    val rol: RolUsuario?
        get() = usuario?.rol

    val puedeRecibirNotificacionStock: Boolean
        get() = rol == RolUsuario.ADMINISTRADOR ||
                rol == RolUsuario.ENCARGADO_INVENTARIO

    fun iniciarSesion(usuario: Usuario) {
        this.usuario = usuario
    }

    fun cerrarSesion() {
        usuario = null
    }
}