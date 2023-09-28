package data.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val description: String,
    val id: String,
    @SerialName("created_time")
    val createdTime: Long,
    @SerialName("thumbnail_240_url")
    val thumbnailUrl: String,
    val title: String
)

val Video.createdTimeInstant get() = Instant.fromEpochSeconds(createdTime)