package cl.kaiju.appkaiju.model.login

import cl.kaiju.appkaiju.model.usuario.RolUsuario
import cl.kaiju.appkaiju.model.usuario.Usuario

object SesionActual {

    var usuario: Usuario? = null

    // Token JWT del backend (null si no hay sesión o es invitado)
    var token: String? = null

    val rol: RolUsuario?
        get() = usuario?.rol

    val puedeRecibirNotificacionStock: Boolean
        get() = rol == RolUsuario.ADMINISTRADOR ||
                rol == RolUsuario.ENCARGADO_INVENTARIO

    fun iniciarSesion(usuario: Usuario, token: String? = null) {
        this.usuario = usuario
        this.token = token
    }

    fun cerrarSesion() {
        usuario = null
        token = null
    }
}