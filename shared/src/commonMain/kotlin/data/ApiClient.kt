package data

import data.models.Video
import data.models.VideoResponse

interface ApiClient {
    suspend fun getVideos(): Result<List<Video>>
    suspend fun getVideo(id: String)

}