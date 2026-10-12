package cl.kaiju.appkaiju.viewmodel

import androidx.lifecycle.ViewModel
import cl.kaiju.appkaiju.model.Producto
import cl.kaiju.appkaiju.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventarioViewModel(
    private val repository: InventarioRepository = InventarioRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        _productos.value = repository.obtenerProductos()
    }
}