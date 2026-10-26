package cl.kaiju.appkaiju.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
    val context = LocalContext.current.applicationContext

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
        loginViewModel.cerrarSesion()
        inventarioViewModel.onBusquedaChange("") // limpia la busqueda
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
        startDestination = Rutas.Login.ruta,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 700 }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -700 }) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -700 }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 700 }) + fadeOut()
        }
    ) {
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

        composable(Rutas.Catalogo.ruta) {
            PantallaCatalogo(
                inventarioViewModel = inventarioViewModel,
                onVolver = cerrarSesion,
                onIrADetalle = { sku ->
                    navController.navigate(Rutas.DetalleProducto.crearRuta(sku, true))
                }
            )
        }

        composable(Rutas.Inventario.ruta) {
            RutaProtegida(rol, Rutas.Inventario.ruta, irALogin) {
                    rolActual ->
                PantallaInventario(
                    rol = rolActual,
                    inventarioViewModel = inventarioViewModel,
                    onNavegar = navegarEnBarra,
                    onCerrarSesion = cerrarSesion,
                    onIrAFormulario = { navController.navigate(Rutas.FormularioProducto.ruta) },
                    onIrADetalle = { sku ->
                        navController.navigate(Rutas.DetalleProducto.crearRuta(sku, false))
                    },
                    onIrARegistrarVenta = { navController.navigate(Rutas.RegistrarVenta.ruta) }
                )
            }
        }

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

        composable(Rutas.RegistrarVenta.ruta) {
            RutaProtegida(rol, Rutas.RegistrarVenta.ruta, irALogin) {
                    rolActual ->
                PantallaRegistroVenta(
                    rol = rolActual,
                    onVolver = { navController.popBackStack() }
                )
            }
        }

        composable(Rutas.FormularioProducto.ruta) {
            PantallaFormularioProducto(
                inventarioViewModel = inventarioViewModel,
                onVolver = { navController.popBackStack() },
                onGuardarExitoso = { navController.popBackStack() }
            )
        }

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

        composable(Rutas.Movimiento.ruta) {
            PantallaMovimiento(
                inventarioViewModel = inventarioViewModel,
                onVolver = { navController.popBackStack() },
                onMovimientoRegistrado = { navController.popBackStack() }
            )
        }
    }
}