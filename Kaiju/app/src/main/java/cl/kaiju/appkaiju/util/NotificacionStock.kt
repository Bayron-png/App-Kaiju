
package cl.kaiju.appkaiju.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import cl.kaiju.appkaiju.R
import cl.kaiju.appkaiju.model.producto.Producto

object NotificacionStock {

    private const val ID_CANAL = "stock_bajo"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                ID_CANAL,
                "Alertas de inventario",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Avisos cuando un producto alcanza su stock mínimo"
            }

            val administrador = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            administrador.createNotificationChannel(canal)
        }
    }

    fun mostrar(context: Context, producto: Producto) {
        // En Android 13 o superior se necesita permiso de notificaciones
        if (
            Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        crearCanal(context)

        val notificacion = NotificationCompat.Builder(context, ID_CANAL)
            .setSmallIcon(R.drawable.ic_notificacion_stock)
            .setContentTitle("Stock bajo: ${producto.nombre}")
            .setContentText(
                "Quedan ${producto.cantidadDisponible} unidades disponibles."
            )
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "El producto ${producto.nombre} alcanzó su stock mínimo. " +
                            "Stock actual: ${producto.cantidadDisponible} unidades. " +
                            "Stock mínimo: ${producto.stockMinimo}."
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(
                producto.sku.hashCode().let {
                    if (it == 0) 1 else it
                },
                notificacion
            )
        vibrar(context)
    }

    private fun vibrar(context: Context) {
        @Suppress("DEPRECATION")
        val vibrador = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (!vibrador.hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrador.vibrate(
                VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrador.vibrate(400)
        }
    }
}
