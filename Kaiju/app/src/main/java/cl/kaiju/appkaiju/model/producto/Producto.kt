package cl.kaiju.appkaiju.model.producto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey val sku: String, // Código del producto
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val cantidadDisponible: Int,
    val stock: Int,
    val stockMinimo: Int
) {
    val estadoStock: String
        get() = when {
            cantidadDisponible <= 0 -> "Producto agotado"
            cantidadDisponible <= stockMinimo -> "Producto con pocas unidades"
            else -> "Producto disponible"
        }
}