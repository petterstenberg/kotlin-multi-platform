import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.BirdImage

class BirdsViewModel(private val repository: BirdsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.BirdsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateImages()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun updateImages() {
        viewModelScope.launch {
            repository.images.collectLatest { result ->
                result.onSuccess {
                    _uiState.emit(UiState.BirdsUiState(images = it))
                }.onFailure {
                    _uiState.emit(UiState.ErrorUiState(it))
                }
            }
        }
    }

    fun selectCategory(category: String) {
        when (val state = _uiState.value) {
            is UiState.BirdsUiState -> {
                _uiState.update {
                    state.copy(selectedCategory = category)
                }
            }
            else -> Unit
        }
    }

    override fun onCleared() {
        repository.release()
    }
}

sealed class UiState {
    data class BirdsUiState(
        val images: List<BirdImage> = emptyList(),
        val selectedCategory: String? = null
    ) : UiState()

    data class ErrorUiState(
        val t: Throwable
    ) : UiState()
}

val UiState.BirdsUiState.categories get() = images.map { it.category }.toSet()
val UiState.BirdsUiState.selectedImages get() = images.filter { it.category == selectedCategory }

