package cl.kaiju.appkaiju.viewmodel.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.kaiju.appkaiju.model.login.SesionActual
import cl.kaiju.appkaiju.model.movimiento.Movimiento
import cl.kaiju.appkaiju.model.producto.Producto
import cl.kaiju.appkaiju.repository.catalogo.InventarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventarioViewModel(
    private val repository: InventarioRepository
) : ViewModel() {

    data class EstadoFormularioProducto(
        val sku: String = "SKU-",
        val nombre: String = "",
        val categoria: String = "",
        val precio: String = "",
        val stock: String = "",
        val stockMinimo: String = "1",
        val errorFormulario: String? = null,
        val guardando: Boolean = false
    )

    private val _estadoFormulario = MutableStateFlow(EstadoFormularioProducto())
    val estadoFormulario: StateFlow<EstadoFormularioProducto> = _estadoFormulario.asStateFlow()

    fun onSkuChange(nuevoSku: String) {
        _estadoFormulario.value = _estadoFormulario.value.copy(sku = nuevoSku, errorFormulario = null)
    }

    fun onNombreChange(nuevoNombre: String) {
        _estadoFormulario.value = _estadoFormulario.value.copy(nombre = nuevoNombre, errorFormulario = null)
    }

    fun onCategoriaChange(nuevaCategoria: String) {
        _estadoFormulario.value = _estadoFormulario.value.copy(categoria = nuevaCategoria, errorFormulario = null)
    }

    fun onPrecioChange(nuevoPrecio: String) {
        _estadoFormulario.value = _estadoFormulario.value.copy(precio = nuevoPrecio, errorFormulario = null)
    }

    fun onStockChange(nuevoStock: String) {
        _estadoFormulario.value = _estadoFormulario.value.copy(stock = nuevoStock, errorFormulario = null)
    }

    fun onStockMinimoChange(nuevoStockMinimo: String) {
        if (nuevoStockMinimo.all { it.isDigit() }) {
            _estadoFormulario.value = _estadoFormulario.value.copy(stockMinimo = nuevoStockMinimo, errorFormulario = null)
        }
    }

    fun resetearFormulario() {
        _estadoFormulario.value = EstadoFormularioProducto()
    }

    suspend fun guardarProducto(): Boolean {
        val actual = _estadoFormulario.value
        val skuLimpio = actual.sku.trim()
        val precioDouble = actual.precio.toDoubleOrNull()
        val stockInt = actual.stock.toIntOrNull()
        val stockMinimoInt = actual.stockMinimo.toIntOrNull()

        val mensajeError = when {
            skuLimpio.isBlank() || skuLimpio == "SKU-" -> "Ingresa un SKU válido."
            actual.nombre.isBlank() -> "Ingresa el nombre del producto."
            actual.categoria.isBlank() -> "Ingresa la categoría."
            precioDouble == null || !precioDouble.isFinite() || precioDouble < 0 -> "Ingresa un precio válido."
            stockInt == null || stockInt < 0 -> "Ingresa un stock inicial válido."
            stockMinimoInt == null || stockMinimoInt < 0 -> "Ingresa un stock mínimo válido."
            else -> null
        }

        if (mensajeError != null) {
            _estadoFormulario.value = actual.copy(errorFormulario = mensajeError)
            return false
        }

        _estadoFormulario.value = actual.copy(guardando = true, errorFormulario = null)
        return try {
            val nuevoProducto = Producto(
                sku = skuLimpio,
                nombre = actual.nombre.trim(),
                categoria = actual.categoria.trim(),
                precio = precioDouble!!,
                cantidadDisponible = stockInt!!,
                stock = stockInt,
                stockMinimo = stockMinimoInt!!
            )
            agregarProducto(nuevoProducto)
            true
        } catch (e: Exception) {
            _estadoFormulario.value = actual.copy(
                guardando = false,
                errorFormulario = "No se pudo guardar el producto. Inténtalo nuevamente."
            )
            false
        }
    }

    val productos: StateFlow<List<Producto>> = repository.productos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _busqueda = MutableStateFlow("")

    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    fun onBusquedaChange(texto: String){
        _busqueda.value = texto.take(40) // largo maximo
    }

    val productosFiltrados: StateFlow<List<Producto>> =
        combine(productos, _busqueda){
            lista, texto ->
            val consulta = texto.trim()
            if (consulta.isEmpty()){
                lista
            } else {
                lista.filter {
                    it.nombre.contains(consulta, ignoreCase = true) ||
                    it.sku.contains(consulta, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var skuSeleccionado: String = ""
        private set

    fun seleccionarSku(sku: String) {
        skuSeleccionado = sku
    }

    suspend fun registrarMovimiento(
        sku: String,
        esEntrada: Boolean,
        cantidad: Int
    ): Producto? {
        val productoActual = productos.value.find{ it.sku == sku }
            ?: return null

        val nuevaCantidad = if (esEntrada) {
            productoActual.cantidadDisponible + cantidad
        } else {
            (productoActual.cantidadDisponible - cantidad).coerceAtLeast(0)
        }
        val productoActualizado = productoActual.copy(
            cantidadDisponible = nuevaCantidad
        )

        repository.actualizarProducto(productoActualizado)

        val movimiento = Movimiento(
            skuProducto = sku,
            tipo = if (esEntrada) "Entrada" else "Salida",
            cantidad = cantidad,
            motivo = if (esEntrada) "Reabastecimiento" else "Ajuste / Venta",
            fecha = System.currentTimeMillis()
        )

        repository.insertarMovimiento(movimiento)

        val pasoAStockBajo =
            productoActual.cantidadDisponible > productoActual.stockMinimo &&
                    productoActualizado.cantidadDisponible <= productoActualizado.stockMinimo

        val llegoAStockCero =
            productoActual.cantidadDisponible > 0 &&
                    productoActualizado.cantidadDisponible == 0

        return if (
            (pasoAStockBajo || llegoAStockCero) &&
            SesionActual.puedeRecibirNotificacionStock
        ) {
            productoActualizado
        } else {
            null
        }
    }
    suspend fun agregarProducto(nuevoProducto: Producto) {
        repository.insertarProducto(nuevoProducto)
    }
    fun obtenerMovimientosPorProducto(sku: String): Flow<List<Movimiento>> {
        return repository.obtenerMovimientosPorProducto(sku)
    }
    fun eliminarProducto(
        sku: String,
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            repository.eliminarProducto(sku)
            onExito()
        }
    }
}
