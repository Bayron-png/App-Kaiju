package cl.kaiju.appkaiju.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PokeApiClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    // Cliente OkHttp personalizado con timeout de 20 segundos
    private val clienteHttp = OkHttpClient.Builder()
        .callTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // Una sola instancia se reutiliza en todas las búsquedas.
    // lazy posterga la construcción hasta el primer acceso y conserva el resultado.
    val api: PokeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteHttp) // Conectar el cliente OkHttp
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi::class.java)
    }
}