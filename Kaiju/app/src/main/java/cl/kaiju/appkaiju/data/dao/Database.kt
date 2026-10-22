package cl.kaiju.appkaiju.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.kaiju.appkaiju.model.producto.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Producto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaiju_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            ioThread {
                                val dao = INSTANCE?.productoDao()
                                dao?.let {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        it.insertarProducto(Producto(sku = "SKU-101", nombre = "Polera Roja Kaiju", categoria = "Ropa", precio = 15000.0, cantidadDisponible = 18, stock = 20, stockMinimo = 5))
                                        it.insertarProducto(Producto(sku = "SKU-102", nombre = "Polera Azul Kaiju", categoria = "Ropa", precio = 16000.0, cantidadDisponible = 3, stock = 20, stockMinimo = 5)) // <-- Generará alerta (3 < 5)
                                        it.insertarProducto(Producto(sku = "SKU-103", nombre = "Polera Blanca Kaiju", categoria = "Ropa", precio = 17000.0, cantidadDisponible = 0, stock = 15, stockMinimo = 10)) // <-- Generará alerta (0 < 10)
                                        it.insertarProducto(Producto(sku = "SKU-104", nombre = "Polera Verde Kaiju", categoria = "Ropa", precio = 18000.0, cantidadDisponible = 12, stock = 20, stockMinimo = 4))
                                        it.insertarProducto(Producto(sku = "SKU-105", nombre = "Polera Morada Kaiju", categoria = "Ropa", precio = 19000.0, cantidadDisponible = 2, stock = 20, stockMinimo = 8)) // <-- Generará alerta (2 < 8)
                                    }
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun ioThread(f: () -> Unit) {
            f()
        }
    }
}