
package cl.kaiju.appkaiju.viewmodel.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var skuSeleccionado: String = ""
        private set

    fun seleccionarSku(sku: String) {
        skuSeleccionado = sku
    }

    fun obtenerMovimientosPorProducto(sku: String): Flow<List<Movimiento>> {
        return repository.obtenerMovimientosPorProducto(sku)
    }

    fun registrarMovimiento(
        sku: String,
        tipo: String,
        cantidad: Int,
        motivo: String
    ) {
        viewModelScope.launch {
            val productoActual = productos.value.firstOrNull { it.sku == sku }
                ?: return@launch

            if (cantidad <= 0) return@launch

            val nuevaCantidad = when (tipo) {
                "Entrada" -> productoActual.cantidadDisponible + cantidad
                "Salida" -> {
                    if (cantidad > productoActual.cantidadDisponible) {
                        return@launch
                    }
                    productoActual.cantidadDisponible - cantidad
                }
                "Ajuste" -> cantidad
                else -> return@launch
            }

            val productoActualizado = productoActual.copy(
                cantidadDisponible = nuevaCantidad
            )

            repository.actualizarProducto(productoActualizado)

            repository.insertarMovimiento(
                Movimiento(
                    skuProducto = sku,
                    tipo = tipo,
                    cantidad = cantidad,
                    motivo = motivo.trim()
                )
            )
        }
    }

    fun agregarProducto(nuevoProducto: Producto) {
        viewModelScope.launch {
            repository.insertarProducto(nuevoProducto)
        }
    }
}
