package com.example.amphibiansapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.amphibiansapp.AmphibianImagesApplication
import com.example.amphibiansapp.data.AmphibianImagesRepository
import com.example.amphibiansapp.model.AmphibianImage
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Status States
sealed interface AmphibianUiState {
    data class Success(val amphibians: List<AmphibianImage>) : AmphibianUiState
    object Error: AmphibianUiState
    object Loading: AmphibianUiState
}

class AmphibianViewModel(
    private val amphibianImagesRepository: AmphibianImagesRepository
) : ViewModel() {
    var amphibianUiState: AmphibianUiState by mutableStateOf(AmphibianUiState.Loading)
        private set

    init {
        getAmphibians()
    }

    fun getAmphibians(){
        viewModelScope.launch {
            amphibianUiState = AmphibianUiState.Loading
            amphibianUiState = try {
                AmphibianUiState.Success(amphibianImagesRepository.getAmphibians())
            } catch(e: IOException){
                AmphibianUiState.Error
            } catch (e: HttpException) {
                AmphibianUiState.Error
            }
        }
    }

    /**
     * Factory for [AmphibianViewModel] that takes [AmphibianImagesRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                //A CreationExtras.Key to query an application in which ViewModel is being created.
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AmphibianImagesApplication)
                val amphibianImagesRepository = application.container.amphibianImagesRepository
                AmphibianViewModel(amphibianImagesRepository = amphibianImagesRepository)
            }
        }
    }
}