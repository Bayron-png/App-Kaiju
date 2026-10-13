package cl.kaiju.appkaiju

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.kaiju.appkaiju.navigation.Rutas
import cl.kaiju.appkaiju.ui.screen.*
import cl.kaiju.appkaiju.ui.theme.KaijuDevCatTheme
import cl.kaiju.appkaiju.viewmodel.LoginViewModel
import cl.kaiju.appkaiju.viewmodel.InventarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaijuDevCatTheme {
                val navController = rememberNavController()
                val loginViewModel: LoginViewModel = viewModel()
                val inventarioViewModel: InventarioViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = Rutas.Login.ruta
                ) {
                    // 1. Login
                    composable(Rutas.Login.ruta) {
                        PantallaLogin(
                            loginViewModel = loginViewModel,
                            onLoginExitoso = {
                                navController.navigate(Rutas.Catalogo.ruta) {
                                    popUpTo(Rutas.Login.ruta) { inclusive = true }
                                }
                            }
                        )
                    }

                    // 2. Catálogo (Inventario)
                    composable(Rutas.Catalogo.ruta) {
                        PantallaInventario(
                            inventarioViewModel = inventarioViewModel,
                            onCerrarSesion = {
                                navController.navigate(Rutas.Login.ruta) {
                                    popUpTo(Rutas.Catalogo.ruta) { inclusive = true }
                                }
                            },
                            onIrAFormulario = { navController.navigate(Rutas.FormularioProducto.ruta) },
                            onIrADetalle = { sku -> navController.navigate(Rutas.DetalleProducto.crearRuta(sku)) },
                            onIrAAlertas = { navController.navigate(Rutas.Alertas.ruta) }
                        )
                    }

                    // 3. Formulario Producto (Admin - RF-05)
                    composable(Rutas.FormularioProducto.ruta) {
                        PantallaFormularioProducto(
                            onVolver = { navController.popBackStack() },
                            onGuardarExitoso = { navController.popBackStack() }
                        )
                    }

                    // 4. Detalle Producto (RF-04)
                    composable(
                        route = Rutas.DetalleProducto.ruta,
                        arguments = listOf(navArgument("sku") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val sku = backStackEntry.arguments?.getString("sku") ?: "SKU-GENERICO"
                        PantallaDetalleProducto(
                            skuProducto = sku,
                            onVolver = { navController.popBackStack() },
                            onIrAMovimiento = { navController.navigate(Rutas.Movimiento.ruta) }
                        )
                    }

                    // 5. Movimiento (RF-06, RF-07, RF-08)
                    composable(Rutas.Movimiento.ruta) {
                        PantallaMovimiento(
                            onVolver = { navController.popBackStack() },
                            onMovimientoRegistrado = { navController.popBackStack() }
                        )
                    }

                    // 6. Alertas (RF-09)
                    composable(Rutas.Alertas.ruta) {
                        PantallaAlertas(
                            inventarioViewModel = inventarioViewModel, // <-- Pasar la misma instancia de ViewModel
                            onVolver = { navController.popBackStack() },
                            onSeleccionarAlerta = { sku ->
                                navController.navigate(Rutas.DetalleProducto.crearRuta(sku))
                            }
                        )
                    }
                }
            }
        }
    }
}