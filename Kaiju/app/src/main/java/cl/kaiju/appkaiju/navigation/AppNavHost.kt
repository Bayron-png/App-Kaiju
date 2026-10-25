package cl.kaiju.appkaiju.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.kaiju.appkaiju.data.dao.AppDatabase
import cl.kaiju.appkaiju.repository.catalogo.InventarioRepository
import cl.kaiju.appkaiju.ui.screen.alertas.PantallaAlertas
import cl.kaiju.appkaiju.ui.screen.catalogo.PantallaCatalogo
import cl.kaiju.appkaiju.ui.screen.formulario.PantallaFormularioProducto
import cl.kaiju.appkaiju.ui.screen.inventario.PantallaDetalleProducto
import cl.kaiju.appkaiju.ui.screen.inventario.PantallaInventario
import cl.kaiju.appkaiju.ui.screen.login.PantallaLogin
import cl.kaiju.appkaiju.ui.screen.movimientos.PantallaMovimiento
import cl.kaiju.appkaiju.ui.screen.personalventas.PantallaRegistroVenta
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel
import cl.kaiju.appkaiju.viewmodel.login.LoginViewModel

@Composable
fun AppNavHost() {
    // Contexto de toda la app (vive mientras la app esta abierta)
    val context = LocalContext.current.applicationContext

    // Room necesita el contexto para abrir la base de datos
    val database = AppDatabase.obtenerInstancia(context)
    val repository = InventarioRepository(productoDao = database.productoDao(), movimientoDao = database.movimientoDao())

    val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InventarioViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InventarioViewModel(repository) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }

    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val inventarioViewModel: InventarioViewModel = viewModel(factory = viewModelFactory)

    val estadoLogin by loginViewModel.estado.collectAsState()
    val rol = estadoLogin.usuarioAutenticado?.rol

    val irALogin: () -> Unit = {
        if (navController.currentDestination?.route != Rutas.Login.ruta) {
            navController.navigate(Rutas.Login.ruta){
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

    val cerrarSesion: () -> Unit = {
        loginViewModel.cerrarSesion() // borra rol, usuario y campos
        irALogin()
    }

    val navegarEnBarra: (String) -> Unit = {
        destino -> rol?.let {
            rolActual ->
            navController.navigate(destino){
                popUpTo(Destinos.rutaInicio(rolActual))
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.Login.ruta
    ) {
        // LOGIN
        composable(Rutas.Login.ruta) {
            PantallaLogin(
                loginViewModel = loginViewModel,
                onLoginExitoso = {
                    val rolLogueado = loginViewModel.estado.value.usuarioAutenticado?.rol
                    if (rolLogueado != null){
                        navController.navigate(Destinos.rutaInicio(rolLogueado)){
                            popUpTo(Rutas.Login.ruta) { inclusive = true }
                        }
                    }
                },
                onIngresarComoInvitado = {
                    navController.navigate(Rutas.Catalogo.ruta)
                }
            )
        }

        // CATALOGO PARA INVITADOS
        composable(Rutas.Catalogo.ruta) {
            PantallaCatalogo(
                inventarioViewModel = inventarioViewModel,
                onVolver = cerrarSesion,
                onIrADetalle = { sku ->
                    navController.navigate(Rutas.DetalleProducto.crearRuta(sku, true))
                }
            )
        }

        // INVENTARIO PARA USUARIOS CON ACCESO
        composable(Rutas.Inventario.ruta) {
            RutaProtegida(rol, Rutas.Inventario.ruta, irALogin) {
                rolActual ->
                PantallaInventario(
                    rol = rolActual,
                    inventarioViewModel = inventarioViewModel,
                    onNavegar = navegarEnBarra,
                    onCerrarSesion = cerrarSesion, // uso de funcion nueva
                    onIrAFormulario = { navController.navigate(Rutas.FormularioProducto.ruta) },
                    onIrADetalle = { sku ->
                        navController.navigate(Rutas.DetalleProducto.crearRuta(sku, false))
                    },
                    onIrARegistrarVenta = { navController.navigate(Rutas.RegistrarVenta.ruta) }
                )
            }
        }

        // ALERTAS
        composable(Rutas.Alertas.ruta) {
            RutaProtegida(rol, Rutas.Alertas.ruta, irALogin) {
                rolActual ->
                PantallaAlertas(
                    rol = rolActual,
                    inventarioViewModel = inventarioViewModel,
                    onNavegar = navegarEnBarra,
                    onCerrarSesion = cerrarSesion,
                    onSeleccionarAlerta = { sku ->
                        navController.navigate(Rutas.DetalleProducto.crearRuta(sku, false))
                    }
                )
            }
        }

        // REGISTRAR VENTA ( No tiene contenido aun )
        composable(Rutas.RegistrarVenta.ruta) {
            RutaProtegida(rol, Rutas.RegistrarVenta.ruta, irALogin) {
                rolActual ->
                PantallaRegistroVenta(
                    rol = rolActual,
                    onVolver = {navController.popBackStack()}
                )
            }
        }

        // FORMULARIO DE PRODUCTO
        composable(Rutas.FormularioProducto.ruta) {
            PantallaFormularioProducto(
                inventarioViewModel = inventarioViewModel,
                onVolver = { navController.popBackStack() },
                onGuardarExitoso = { navController.popBackStack() }
            )
        }

        // DETALLE DEL PRODUCTO
        composable(
            route = Rutas.DetalleProducto.ruta,
            arguments = listOf(
                navArgument("sku") {
                    type = NavType.StringType
                },
                navArgument("esInvitado") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val sku = backStackEntry.arguments?.getString("sku")
                ?: "SKU-GENERICO"

            val esInvitado = backStackEntry.arguments
                ?.getBoolean("esInvitado") ?: false

            PantallaDetalleProducto(
                skuProducto = sku,
                inventarioViewModel = inventarioViewModel,
                esInvitado = esInvitado,
                onVolver = {
                    navController.popBackStack()
                },
                onIrAMovimiento = {
                    if (!esInvitado) {
                        navController.navigate(Rutas.Movimiento.ruta)
                    }
                }
            )
        }

        // MOVIMIENTOS
        composable(Rutas.Movimiento.ruta) {
            PantallaMovimiento(
                inventarioViewModel = inventarioViewModel,
                onVolver = { navController.popBackStack() },
                onMovimientoRegistrado = { navController.popBackStack() }
            )
        }
    }
}
