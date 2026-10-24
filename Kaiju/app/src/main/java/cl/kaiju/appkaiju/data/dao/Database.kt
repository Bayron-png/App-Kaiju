package cl.kaiju.appkaiju.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.kaiju.appkaiju.model.movimiento.Movimiento
import cl.kaiju.appkaiju.model.producto.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Producto::class, Movimiento::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao

    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS movimientos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        skuProducto TEXT NOT NULL,
                        tipo TEXT NOT NULL,
                        cantidad INTEGER NOT NULL,
                        motivo TEXT NOT NULL,
                        fecha INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun obtenerInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaiju_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val dao = database.productoDao()

                                    dao.insertarProducto(
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

                                    dao.insertarProducto(
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

                                    dao.insertarProducto(
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

                                    dao.insertarProducto(
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

                                    dao.insertarProducto(
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
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}