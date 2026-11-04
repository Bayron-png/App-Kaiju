package cl.kaiju.appkaiju.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO (Data Transfer Object): Son los campos JSON usados por la app.
 * Gson crea estos objetos al recibir la respuesta; ignora los campos no declarados aquí.
 * Los nombres coinciden con los de PokéAPI.
 */
data class PokemonDto(
    val id: Int,
    val name: String,
    val height: Int, // PokéAPI entrega decímetros.
    val weight: Int, // PokéAPI entrega hectogramos.
    // Un objeto anidado contiene las URLs de las distintas imágenes.
    val sprites: SpritesDto,
    // Cada elemento incluye el orden del tipo y otro objeto con su nombre.
    val types: List<PokemonTypeDto>
)

// El JSON usa snake_case; SerializedName lo vincula a la propiedad camelCase.
// String? permite que la API entregue null cuando no hay imagen disponible.
data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?
)

// slot permite ordenar los tipos al presentarlos. type contiene el nombre del tipo.
data class PokemonTypeDto(val slot: Int, val type: TypeDto)
// Solo necesitamos el nombre; la URL adicional del objeto JSON queda fuera del DTO.
data class TypeDto(val name: String)