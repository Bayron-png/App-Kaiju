
package cl.kaiju.appkaiju.navigation

import androidx.compose.runtime.Composable
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
import cl.kaiju.appkaiju.viewmodel.catalogo.InventarioViewModel
import cl.kaiju.appkaiju.viewmodel.login.LoginViewModel

@Composable
fun AppNavHost() {
    val context = LocalContext.current.applicationContext
    val database = AppDatabase.obtenerInstancia(context)
    val repository = InventarioRepository(database.productoDao())

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
    val inventarioViewModel: InventarioViewModel =
        viewModel(factory = viewModelFactory)

    NavHost(
        navController = navController,
        startDestination = Rutas.Login.ruta
    ) {
        // LOGIN
        composable(Rutas.Login.ruta) {
            PantallaLogin(
                loginViewModel = loginViewModel,
                onLoginExitoso = {
                    navController.navigate(Rutas.Inventario.ruta) {
                        popUpTo(Rutas.Login.ruta) {
                            inclusive = true
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
                onVolver = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Catalogo.ruta) {
                            inclusive = true
                        }
                    }
                },
                onIrADetalle = { sku ->
                    navController.navigate(
                        Rutas.DetalleProducto.crearRuta(sku, true)
                    )
                }
            )
        }

        // INVENTARIO PARA USUARIOS CON ACCESO
        composable(Rutas.Inventario.ruta) {
            PantallaInventario(
                inventarioViewModel = inventarioViewModel,
                onCerrarSesion = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Inventario.ruta) {
                            inclusive = true
                        }
                    }
                },
                onIrAFormulario = {
                    navController.navigate(Rutas.FormularioProducto.ruta)
                },
                onIrADetalle = { sku ->
                    navController.navigate(
                        Rutas.DetalleProducto.crearRuta(sku, false)
                    )
                },
                onIrAAlertas = {
                    navController.navigate(Rutas.Alertas.ruta)
                }
            )
        }

        // FORMULARIO DE PRODUCTO
        composable(Rutas.FormularioProducto.ruta) {
            PantallaFormularioProducto(
                inventarioViewModel = inventarioViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onGuardarExitoso = {
                    navController.popBackStack()
                }
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
                onVolver = {
                    navController.popBackStack()
                },
                onMovimientoRegistrado = {
                    navController.popBackStack()
                }
            )
        }

        // ALERTAS
        composable(Rutas.Alertas.ruta) {
            PantallaAlertas(
                inventarioViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onSeleccionarAlerta = { sku ->
                    navController.navigate(
                        Rutas.DetalleProducto.crearRuta(sku, false)
                    )
                }
            )
        }
    }
}
