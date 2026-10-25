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

    val productos: StateFlow<List<Producto>> = repository.productos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var skuSeleccionado: String = ""
        private set

    fun seleccionarSku(sku: String) {
        skuSeleccionado = sku
    }

    fun registrarMovimiento(sku: String, esEntrada: Boolean, cantidad: Int) {
        viewModelScope.launch {
            val productoActual = productos.value.find { it.sku == sku }
            if (productoActual != null) {
                val nuevaCantidad = if (esEntrada) {
                    productoActual.cantidadDisponible + cantidad
                } else {
                    (productoActual.cantidadDisponible - cantidad).coerceAtLeast(0)
                }
                val productoActualizado = productoActual.copy(cantidadDisponible = nuevaCantidad)
                repository.actualizarProducto(productoActualizado)

                val tipoMovimiento = if (esEntrada) "Entrada" else "Salida"
                val movimiento = Movimiento(
                    skuProducto = sku,
                    tipo = tipoMovimiento,
                    cantidad = cantidad,
                    motivo = if (esEntrada) "Reabastecimiento" else "Ajuste / Venta",
                    fecha = System.currentTimeMillis()
                )
                repository.insertarMovimiento(movimiento)
            }
        }
    }

    suspend fun agregarProducto(nuevoProducto: Producto) {
        repository.insertarProducto(nuevoProducto)
    }

    fun obtenerMovimientosPorProducto(sku: String): Flow<List<Movimiento>> {
        return repository.obtenerMovimientosPorProducto(sku)
    }

    fun eliminarProducto(sku: String, onExito: () -> Unit = {}) {
        viewModelScope.launch {
            repository.eliminarProducto(sku)
            onExito()
        }
    }
}
