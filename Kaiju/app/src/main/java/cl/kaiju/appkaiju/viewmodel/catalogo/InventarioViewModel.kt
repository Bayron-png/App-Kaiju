package cl.kaiju.appkaiju.viewmodel.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.kaiju.appkaiju.model.producto.Producto
import cl.kaiju.appkaiju.repository.catalogo.InventarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventarioViewModel(
    private val repository: InventarioRepository
) : ViewModel() {

    // Aquí se llama a repository.productos (esto hace que se quite el gris)
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
                repository.actualizarProducto(productoActualizado) // Aquí llamas a actualizarProducto
            }
        }
    }

    fun agregarProducto(nuevoProducto: Producto) {
        viewModelScope.launch {
            repository.insertarProducto(nuevoProducto) // Aquí llamas a insertarProducto
        }
    }
}