package cl.kaiju.appkaiju.data.remote

import cl.kaiju.appkaiju.data.model.PokemonDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokemon/{nombreOId}")
    suspend fun obtenerPokemon(
        @Path("nombreOId") nombreOId: String
    ): PokemonDto
}