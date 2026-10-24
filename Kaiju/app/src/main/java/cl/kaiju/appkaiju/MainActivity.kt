package cl.kaiju.appkaiju

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cl.kaiju.appkaiju.navigation.AppNavHost
import cl.kaiju.appkaiju.ui.theme.KaijuDevCatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaijuDevCatTheme {
                AppNavHost()
            }
        }
    }
}
