package cl.kaiju.appkaiju.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.kaiju.appkaiju.model.movimiento.Movimiento
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {

    @Insert
    suspend fun insertarMovimiento(movimiento: Movimiento)

    @Query("""
        SELECT * FROM movimientos
        WHERE skuProducto = :sku
        ORDER BY fecha DESC
    """)
    fun obtenerMovimientosPorProducto(sku: String): Flow<List<Movimiento>>

    @Query("DELETE FROM movimientos WHERE skuProducto = :sku")
    suspend fun eliminarMovimientosPorProducto(sku: String)
}