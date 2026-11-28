package cl.kaiju.appkaiju.data.remote

import cl.kaiju.appkaiju.model.login.SesionActual
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object  KaijuApiClient {

    // IMPORTANTE: debe terminar en "/".
    //  - Emulador de Android Studio -> el PC se ve como 10.0.2.2
    //  - Celular físico             -> IP local del PC, ej. http://192.168.1.50:3000/api/v1/
    //  - Servidor en AWS            -> http://IP_PUBLICA:3000/api/v1/
    const val BASE_URL = "http://localhost:3000/api/v1/"

    // Agrega el token JWT a cada petición cuando hay una sesión iniciada.
    // Solo se usa en este cliente: la PokeAPI tiene su propio cliente y nunca recibe el token.
    private val agregarToken = Interceptor { chain ->
        val token = SesionActual.token
        val peticion = if (token != null) {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        chain.proceed(peticion)
    }

    private val clienteHttp = OkHttpClient.Builder()
        .addInterceptor(agregarToken)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val api: KaijuApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KaijuApi::class.java)
    }
}
