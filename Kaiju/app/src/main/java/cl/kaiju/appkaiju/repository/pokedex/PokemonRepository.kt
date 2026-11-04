package cl.kaiju.appkaiju.repository.pokedex

import cl.kaiju.appkaiju.data.model.PokemonDto
import cl.kaiju.appkaiju.data.remote.PokeApi
import cl.kaiju.appkaiju.data.remote.PokeApiClient

class PokemonRepository(
    private val api: PokeApi = PokeApiClient.api
) {
    // Caché en memoria: guarda resultados por nombre (minúscula) e ID.
    private val cache = mutableMapOf<String, PokemonDto>()

    suspend fun buscarPokemon(busqueda: String): PokemonDto {
        val clave = busqueda.trim().lowercase()

        // 1. Si está en caché, se retorna directo sin gastar red
        cache[clave]?.let { return it }

        // 2. Si no, consulta a la red
        val resultado = api.obtenerPokemon(clave)

        // 3. Guarda en caché tanto por nombre como por ID
        cache[resultado.name.lowercase()] = resultado
        cache[resultado.id.toString()] = resultado

        return resultado
    }
}