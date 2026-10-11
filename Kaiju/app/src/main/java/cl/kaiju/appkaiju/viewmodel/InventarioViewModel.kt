package cl.kaiju.appkaiju.viewmodel

import androidx.lifecycle.ViewModel
import cl.kaiju.appkaiju.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventarioViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        _productos.value = listOf(
            Producto("SKU-101", "Polera Roja Kaiju", "Ropa", 15000.0, 18, 5),
            Producto("SKU-102", "Polera Azul Kaiju", "Ropa", 16000.0, 3, 5),
            Producto("SKU-103", "Polera Blanca Kaiju", "Ropa", 17000.0, 0, 10),
            Producto("SKU-104", "Polera Verde Kaiju", "Ropa", 18000.0, 12, 4),
            Producto("SKU-105", "Polera Morada Kaiju", "Ropa", 19000.0, 2, 8)
        )
    }
}