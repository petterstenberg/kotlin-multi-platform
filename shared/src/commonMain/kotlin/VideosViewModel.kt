import data.models.createdTimeInstant
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class VideosViewModel(private val repository: VideosRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.VideosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateVideos()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun updateVideos() {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)

            repository.getVideos().onSuccess { result ->
                val videos = result.getOrNull() ?: emptyList()
                _uiState.emit(UiState.VideosUiState(videos = videos.map { video ->
                    val localCreatedDate =
                        video.createdTimeInstant.toLocalDateTime(TimeZone.currentSystemDefault())
                    UiVideo(
                        id = video.id,
                        title = video.title,
                        description = video.description,
                        createdDate = "${localCreatedDate.dayOfMonth} ${
                            localCreatedDate.month.name.lowercase()
                                .replaceFirstChar { it.uppercaseChar() }
                        } ${localCreatedDate.year}",
                        thumbnailUrl = video.thumbnailUrl
                    )
                }))
            }.onFailure {
                _uiState.emit(UiState.ErrorUiState(it))
            }
        }
    }
}

sealed class UiState {
    data object Loading: UiState()
    data class VideosUiState(
        val videos: List<UiVideo> = emptyList()
    ) : UiState()

    data class ErrorUiState(
        val t: Throwable
    ) : UiState()
}

data class UiVideo(
    val id: String,
    val title: String,
    val description: String,
    val createdDate: String,
    val thumbnailUrl: String,
)

