package cl.kaiju.appkaiju.viewmodel.pokedex

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.kaiju.appkaiju.repository.pokedex.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PokedexViewModel(
    private val repository: PokemonRepository = PokemonRepository()) : ViewModel() {

    // Texto ingresado por el usuario en el buscador
    var textoBusqueda by mutableStateOf("")
        private set

    // Estado reactivo de la pantalla
    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Vacio)
    val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow()

    fun actualizarTextoBusqueda(nuevoTexto: String) {
        textoBusqueda = nuevoTexto
    }

    fun buscarPokemon() {
        val consulta = textoBusqueda.trim()
        if (consulta.isEmpty()) {
            _uiState.value = PokedexUiState.Error("Por favor ingresa un nombre o número de Pokémon.")
            return
        }

        viewModelScope.launch {
            _uiState.value = PokedexUiState.Cargando
            try {
                val pokemon = repository.buscarPokemon(consulta)
                _uiState.value = PokedexUiState.Exito(pokemon)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    _uiState.value = PokedexUiState.Error("Pokémon '$consulta' no encontrado.")
                } else {
                    _uiState.value = PokedexUiState.Error("Error en el servidor: ${e.code()}")
                }
            } catch (e: IOException) {
                _uiState.value = PokedexUiState.Error("Sin conexión a Internet.")
            } catch (e: Exception) {
                _uiState.value = PokedexUiState.Error("Ocurrió un error inesperado: ${e.localizedMessage}")
            }
        }
    }
}