package cl.kaiju.appkaiju.viewmodel.pokedex

import cl.kaiju.appkaiju.data.model.PokemonDto

sealed interface PokedexUiState {
    object Vacio : PokedexUiState
    object Cargando : PokedexUiState
    data class Exito(val pokemon: PokemonDto) : PokedexUiState
    data class Error(val mensaje: String) : PokedexUiState
}