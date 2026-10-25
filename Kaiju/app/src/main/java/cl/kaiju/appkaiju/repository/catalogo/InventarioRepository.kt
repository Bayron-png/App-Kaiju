
package cl.kaiju.appkaiju.repository.catalogo

import cl.kaiju.appkaiju.data.dao.MovimientoDao
import cl.kaiju.appkaiju.data.dao.ProductoDao
import cl.kaiju.appkaiju.model.movimiento.Movimiento
import cl.kaiju.appkaiju.model.producto.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class InventarioRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao
) {

    val productos: Flow<List<Producto>> = productoDao.obtenerProductos()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val listaActual = productoDao.obtenerProductos().first()

            if (listaActual.isEmpty()) {
                productoDao.insertarProducto(
                    Producto(
                        sku = "SKU-101",
                        nombre = "Polera Roja Kaiju",
                        categoria = "Ropa",
                        precio = 15000.0,
                        cantidadDisponible = 18,
                        stock = 20,
                        stockMinimo = 5
                    )
                )

                productoDao.insertarProducto(
                    Producto(
                        sku = "SKU-102",
                        nombre = "Polera Azul Kaiju",
                        categoria = "Ropa",
                        precio = 16000.0,
                        cantidadDisponible = 3,
                        stock = 20,
                        stockMinimo = 5
                    )
                )

                productoDao.insertarProducto(
                    Producto(
                        sku = "SKU-103",
                        nombre = "Polera Blanca Kaiju",
                        categoria = "Ropa",
                        precio = 17000.0,
                        cantidadDisponible = 0,
                        stock = 15,
                        stockMinimo = 10
                    )
                )

                productoDao.insertarProducto(
                    Producto(
                        sku = "SKU-104",
                        nombre = "Polera Verde Kaiju",
                        categoria = "Ropa",
                        precio = 18000.0,
                        cantidadDisponible = 12,
                        stock = 20,
                        stockMinimo = 4
                    )
                )

                productoDao.insertarProducto(
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
    }

    suspend fun insertarProducto(producto: Producto) {
        productoDao.insertarProducto(producto)
    }

    suspend fun actualizarProducto(producto: Producto) {
        productoDao.actualizarProducto(producto)
    }

    suspend fun insertarMovimiento(movimiento: Movimiento) {
        movimientoDao.insertarMovimiento(movimiento)
    }

    suspend fun eliminarProducto(sku: String) {
        movimientoDao.eliminarMovimientosPorProducto(sku)
        productoDao.eliminarProducto(sku)
    }

    suspend fun existeSku(sku: String): Boolean {
        return productoDao.existeSku(sku.trim())
    }

    fun obtenerMovimientosPorProducto(sku: String): Flow<List<Movimiento>> {
        return movimientoDao.obtenerMovimientosPorProducto(sku)
    }
}
