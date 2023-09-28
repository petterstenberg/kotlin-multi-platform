package data

import data.models.Video
import data.models.VideoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

private const val BASE_URL = "https://api.dailymotion.com"
class ApiClientImpl : ApiClient {
    private val httpClient = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json()
        }

        install(HttpCache) {
            publicStorage(CacheStorage.Unlimited())
        }

        defaultRequest {
            // add base url for all request
            url(BASE_URL)
        }
    }

    override suspend fun getVideos(): Result<List<Video>> = kotlin.runCatching {
        httpClient.get("/videos?fields=description%2Ctitle%2Cid%2Ccreated_time%2Cthumbnail_240_url&limit=100&country=se")
    }.map {
        it.body<VideoResponse>().list
    }

    override suspend fun getVideo(id: String) {
        httpClient.get("/video/$id")
    }
}