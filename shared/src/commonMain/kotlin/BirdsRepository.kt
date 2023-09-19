import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.flow
import model.BirdImage

class BirdsRepository {

    private val httpClient = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json()
        }
    }

    val images = flow<Result<List<BirdImage>>> {
        kotlin.runCatching {
            httpClient.get("https://sebi.io/demo-image-api/pictures.json")
        }.onSuccess {
            val images = it.body<List<BirdImage>>()

            emit(Result.success(images))
        }.onFailure {
            emit(Result.failure(it))
        }
    }

    fun release() {
        httpClient.close()
    }
}
