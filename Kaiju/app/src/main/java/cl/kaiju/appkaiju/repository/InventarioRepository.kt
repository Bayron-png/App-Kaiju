package cl.kaiju.appkaiju.repository

import cl.kaiju.appkaiju.model.producto.Producto

class InventarioRepository {
    fun obtenerProductos(): List<Producto> {
        return listOf(
            Producto(sku = "SKU-101", nombre = "Polera Roja Kaiju", categoria = "Ropa", precio = 15000.0, cantidadDisponible = 18, stock = 20, stockMinimo = 5),
            Producto(sku = "SKU-102", nombre = "Polera Azul Kaiju", categoria = "Ropa", precio = 16000.0, cantidadDisponible = 3, stock = 20, stockMinimo = 5),
            Producto(sku = "SKU-103", nombre = "Polera Blanca Kaiju", categoria = "Ropa", precio = 17000.0, cantidadDisponible = 0, stock = 15, stockMinimo = 10),
            Producto(sku = "SKU-104", nombre = "Polera Verde Kaiju", categoria = "Ropa", precio = 18000.0, cantidadDisponible = 12, stock = 20, stockMinimo = 4),
            Producto(sku = "SKU-105", nombre = "Polera Morada Kaiju", categoria = "Ropa", precio = 19000.0, cantidadDisponible = 2, stock = 20, stockMinimo = 8)
        )
    }
}