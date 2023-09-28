package data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    val explicit: Boolean,
    @SerialName("has_more")
    val hasMore: Boolean,
    val limit: Int,
    val list: List<Video>,
    val page: Int
)