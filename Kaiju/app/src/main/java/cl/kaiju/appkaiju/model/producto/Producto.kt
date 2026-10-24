package cl.kaiju.appkaiju.model.producto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey val sku: String, // Código del producto
    val nombre: String,
    //val descripcion: String,
    val categoria: String,
    //val tipo: String,
    val precio: Double,
    val cantidadDisponible: Int, // eliminar
    val stock: Int, // change for -> stockActual
    val stockMinimo: Int
    //val detalle: String -> texto opcional para talla, color, lote o fecha de vencimiento
) {
    val estadoStock: String
        get() = when {
            cantidadDisponible <= 0 -> "Producto agotado"
            cantidadDisponible <= stockMinimo -> "Producto con pocas unidades"
            else -> "Producto disponible"
        }
}