package cl.kaiju.appkaiju.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Detector de Easter Egg.
 * Cuenta clics consecutivos. Si pasan más de [maxIntervalMs] ms entre clics, el contador se reinicia.
 * Al alcanzar [targetClicks] clics, ejecuta la función [onTrigger].
 */
@Composable
fun rememberEasterEggDetector(
    targetClicks: Int = 10,
    maxIntervalMs: Long = 500L,
    onTrigger: () -> Unit
): () -> Unit {
    var clickCount by remember { mutableIntStateOf(0) }
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime <= maxIntervalMs) {
            clickCount++
        } else {
            clickCount = 1 // Si pasó más de medio segundo, reinicia la cuenta a 1
        }
        lastClickTime = currentTime

        if (clickCount >= targetClicks) {
            clickCount = 0 // Reinicia el contador tras activar el easter egg
            onTrigger()
        }
    }
}