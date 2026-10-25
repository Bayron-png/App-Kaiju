
package cl.kaiju.appkaiju.viewmodel.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.kaiju.appkaiju.model.login.SesionActual
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import cl.kaiju.appkaiju.model.movimiento.Movimiento
import cl.kaiju.appkaiju.model.producto.Producto
import cl.kaiju.appkaiju.repository.catalogo.InventarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventarioViewModel(
    private val repository: InventarioRepository
) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repository.productos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _alertaStockBajo = MutableSharedFlow<Producto>(
        extraBufferCapacity = 1
    )

    val alertaStockBajo = _alertaStockBajo.asSharedFlow()

    var skuSeleccionado: String = ""
        private set

    fun seleccionarSku(sku: String) {
        skuSeleccionado = sku
    }

    fun obtenerMovimientosPorProducto(sku: String): Flow<List<Movimiento>> {
        return repository.obtenerMovimientosPorProducto(sku)
    }


    suspend fun registrarMovimiento(
        sku: String,
        tipo: String,
        cantidad: Int,
        motivo: String
    ): Producto? {
        val productoActual = productos.value.firstOrNull { it.sku == sku }
            ?: return null

        if (cantidad <= 0) return null

        val nuevaCantidad = when (tipo) {
            "Entrada" -> productoActual.cantidadDisponible + cantidad

            "Salida" -> {
                if (cantidad > productoActual.cantidadDisponible) {
                    return null
                }
                productoActual.cantidadDisponible - cantidad
            }

            "Ajuste" -> cantidad
            else -> return null
        }

        val productoActualizado = productoActual.copy(
            cantidadDisponible = nuevaCantidad
        )

        // Guarda el nuevo stock en la base de datos
        repository.actualizarProducto(productoActualizado)

        // Registra el movimiento realizado
        repository.insertarMovimiento(
            Movimiento(
                skuProducto = sku,
                tipo = tipo,
                cantidad = cantidad,
                motivo = motivo.trim()
            )
        )

        // Comprueba si acaba de pasar a stock bajo
        val pasoAStockBajo =
            productoActual.cantidadDisponible > productoActual.stockMinimo &&
                    productoActualizado.cantidadDisponible <= productoActualizado.stockMinimo

        val llegoAStockCero =
            productoActual.cantidadDisponible > 0 &&
                    productoActualizado.cantidadDisponible == 0

        // Solo administrador y encargado de inventario reciben la alerta
        return if (
            (pasoAStockBajo || llegoAStockCero) &&
            SesionActual.puedeRecibirNotificacionStock
        ) {
            productoActualizado
        } else {
            null
        }
    }


    suspend fun agregarProducto(nuevoProducto: Producto): Boolean {
        val sku = nuevoProducto.sku.trim()

        if (sku.isBlank() || repository.existeSku(sku)) {
            return false
        }

        return try {
            repository.insertarProducto(
                nuevoProducto.copy(sku = sku)
            )
            true
        } catch (e: android.database.sqlite.SQLiteConstraintException) {
            false
        }
    }

    fun eliminarProducto(
        sku: String,
        onCompletado: () -> Unit
    ) {
        viewModelScope.launch {
            repository.eliminarProducto(sku)
            onCompletado()
        }
    }
}
