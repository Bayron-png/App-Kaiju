package cl.kaiju.appkaiju.repository

import cl.kaiju.appkaiju.model.Producto

class InventarioRepository {
    fun obtenerProductos(): List<Producto> {
        return listOf(
            Producto("SKU-101", "Polera Roja Kaiju", "Ropa", 15000.0, 18, 5),
            Producto("SKU-102", "Polera Azul Kaiju", "Ropa", 16000.0, 3, 5),
            Producto("SKU-103", "Polera Blanca Kaiju", "Ropa", 17000.0, 0, 10),
            Producto("SKU-104", "Polera Verde Kaiju", "Ropa", 18000.0, 12, 4),
            Producto("SKU-105", "Polera Morada Kaiju", "Ropa", 19000.0, 2, 8)
        )
    }
}