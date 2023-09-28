import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.plugins.ResponseException

@Composable
fun VideosScreen(viewModel: VideosViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(60.dp),
                    strokeWidth = 6.dp,
                    color = Color.Red
                )
            }
            is UiState.VideosUiState -> {
                AnimatedVisibility(state.videos.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        item {
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = "Videos",
                                style = MaterialTheme.typography.h1
                            )
                        }
                        items(state.videos) {
                            VideoRow(it) { videoId ->

                            }
                        }
                    }
                }
            }

            is UiState.ErrorUiState -> {
                val text = when (val e = state.t) {
                    is ResponseException -> {
                        e.response.status.toString()
                    }

                    else -> e.message ?: "Unknown error!"
                }
                Text(text = text)
            }
        }
    }
}

@Composable
fun VideoRow(video: UiVideo, onClick: (id: String) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable { onClick(video.id) },
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        KamelImage(
            modifier = Modifier.height(100.dp).aspectRatio(1.0f).clip(RoundedCornerShape(8.dp)),
            resource = asyncPainterResource(video.thumbnailUrl),
            contentDescription = "${video.title} by ${video.createdDate}",
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.height(100.dp), verticalArrangement = Arrangement.Center) {
            Text(text = video.title, style = MaterialTheme.typography.h3)
            Spacer(Modifier.size(16.dp))
            Text(text = video.createdDate, style = MaterialTheme.typography.subtitle1)
        }
    }
}