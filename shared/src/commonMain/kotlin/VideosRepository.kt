import data.ApiClient

class VideosRepository(private val apiClient: ApiClient) {

    suspend fun getVideos() = kotlin.runCatching {
        apiClient.getVideos()
    }

    suspend fun getVideo(id: String) = kotlin.runCatching {
        apiClient.getVideo(id)
    }

}
