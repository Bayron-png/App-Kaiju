package cl.kaiju.appkaiju.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cl.kaiju.appkaiju.model.producto.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos")
    fun obtenerProductos(): Flow<List<Producto>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarProducto(producto: Producto)

    @Update
    suspend fun actualizarProducto(producto: Producto)

    @Query("DELETE FROM productos WHERE sku = :sku")
    suspend fun eliminarProducto(sku: String)

    @Query("SELECT EXISTS(SELECT 1 FROM productos WHERE sku = :sku)")
    suspend fun existeSku(sku: String): Boolean
}