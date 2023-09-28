import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.ApiClientImpl
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun BirdAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(primary = Color.Black),
        shapes = MaterialTheme.shapes.copy(
            small = AbsoluteCutCornerShape(0.dp),
            medium = AbsoluteCutCornerShape(0.dp),
            large = AbsoluteCutCornerShape(0.dp)
        ),
        typography = Typography(
            h1 = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            h3 = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            ),
            subtitle1 = TextStyle(
                fontSize = 12.sp
            )
        )
    ) {
        content()
    }
}

@Composable
fun App() {
    BirdAppTheme {
        val viewModel = getViewModel(
            Unit,
            viewModelFactory { VideosViewModel(VideosRepository(ApiClientImpl())) }
        )
        VideosScreen(viewModel)
    }
}

expect fun getPlatformName(): String