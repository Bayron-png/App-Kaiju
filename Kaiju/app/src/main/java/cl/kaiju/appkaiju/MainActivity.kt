package cl.kaiju.appkaiju

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.kaiju.appkaiju.navigation.Rutas
import cl.kaiju.appkaiju.ui.screen.PantallaListaInventario
import cl.kaiju.appkaiju.ui.screen.PantallaLogin
import cl.kaiju.appkaiju.ui.theme.KaijuDevCatTheme
import cl.kaiju.appkaiju.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaijuDevCatTheme {
                val navController = rememberNavController()
                val loginViewModel: LoginViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = Rutas.Login.ruta
                ) {
                    composable(Rutas.Login.ruta) {
                        PantallaLogin(
                            loginViewModel = loginViewModel,
                            onLoginExitoso = {
                                navController.navigate(Rutas.Inventario.ruta) {
                                    popUpTo(Rutas.Login.ruta) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Rutas.Inventario.ruta) {
                        PantallaListaInventario(
                            onCerrarSesion = {
                                navController.navigate(Rutas.Login.ruta) {
                                    popUpTo(Rutas.Inventario.ruta) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KaijuDevCatTheme {
        Greeting("Android")
    }
}