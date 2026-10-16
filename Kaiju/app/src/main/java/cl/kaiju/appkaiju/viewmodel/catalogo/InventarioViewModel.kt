package cl.kaiju.appkaiju.viewmodel.catalogo

import androidx.lifecycle.ViewModel
import cl.kaiju.appkaiju.model.producto.Producto
import cl.kaiju.appkaiju.repository.catalogo.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventarioViewModel(
    private val repository: InventarioRepository = InventarioRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    var skuSeleccionado: String = ""
        private set

    fun seleccionarSku(sku: String) {
        skuSeleccionado = sku
    }

    private val _historial = MutableStateFlow<List<String>>(emptyList())
    val historial: StateFlow<List<String>> = _historial.asStateFlow()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        val listaInicial = repository.obtenerProductos()
        _productos.value = if (listaInicial.isNotEmpty()) {
            listaInicial
        } else {
            listOf(
                Producto(
                    sku = "SKU-101",
                    nombre = "Polera Roja Kaiju",
                    categoria = "Ropa",
                    precio = 15000.0,
                    cantidadDisponible = 18,
                    stock = 20,
                    stockMinimo = 5
                ),
                Producto(
                    sku = "SKU-102",
                    nombre = "Polera Azul Kaiju",
                    categoria = "Ropa",
                    precio = 16000.0,
                    cantidadDisponible = 3,
                    stock = 20,
                    stockMinimo = 5
                ),
                Producto(
                    sku = "SKU-103",
                    nombre = "Polera Blanca Kaiju",
                    categoria = "Ropa",
                    precio = 17000.0,
                    cantidadDisponible = 0,
                    stock = 15,
                    stockMinimo = 10
                ),
                Producto(
                    sku = "SKU-104",
                    nombre = "Polera Verde Kaiju",
                    categoria = "Ropa",
                    precio = 18000.0,
                    cantidadDisponible = 12,
                    stock = 20,
                    stockMinimo = 4
                ),
                Producto(
                    sku = "SKU-105",
                    nombre = "Polera Morada Kaiju",
                    categoria = "Ropa",
                    precio = 19000.0,
                    cantidadDisponible = 2,
                    stock = 20,
                    stockMinimo = 8
                )
            )
        }
    }

    fun registrarMovimiento(sku: String, esEntrada: Boolean, cantidad: Int) {
        val listaActual = _productos.value.toMutableList()
        val index = listaActual.indexOfFirst { it.sku == sku }

        if (index != -1) {
            val productoActual = listaActual[index]
            val nuevaCantidad = if (esEntrada) {
                productoActual.cantidadDisponible + cantidad
            } else {
                (productoActual.cantidadDisponible - cantidad).coerceAtLeast(0)
            }

            // Se crea una copia con la nueva cantidad disponible
            listaActual[index] = productoActual.copy(cantidadDisponible = nuevaCantidad)
            _productos.value = listaActual

            val tipoMovimiento = if (esEntrada) "Entrada" else "Salida"
            val registro = "[$tipoMovimiento] SKU: $sku - Cantidad: $cantidad (Stock actual: $nuevaCantidad)"
            _historial.value = _historial.value + registro
        }
    }

    fun agregarProducto(nuevoProducto: Producto) {
        val listaActual = _productos.value.toMutableList()
        listaActual.add(nuevoProducto)
        _productos.value = listaActual
    }
}