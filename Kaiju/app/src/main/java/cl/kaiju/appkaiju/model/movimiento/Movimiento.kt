package cl.kaiju.appkaiju.model.movimiento

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val skuProducto: String,
    val tipo: String,
    val cantidad: Int,
    val motivo: String,
    val fecha: Long = System.currentTimeMillis()
)