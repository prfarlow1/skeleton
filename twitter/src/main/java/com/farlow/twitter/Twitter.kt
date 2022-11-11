package com.farlow.twitter

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

class Twitter(
    private val httpClient: HttpClient,
) {

    lateinit var accessToken: String

    suspend fun obtainAppOnlyAccessToken() {
        val url = "https://api.twitter.com/oauth2/token"
        val response = httpClient.request(url) {
            method = HttpMethod.Post
            headers {
                contentType(ContentType.parse("application/x-www-form-urlencoded;charset=UTF-8"))
                basicAuth(API_KEY, API_KEY_SECRET)
            }
            setBody("grant_type=client_credentials")
        }
        val authResponse = response.body<AuthResponse>()
        accessToken = authResponse.access_token
    }

    suspend fun getLikes(
        userId: String = DEFAULT_USER_ID,
        getLikesRequest: GetLikesRequest = GetLikesRequest()
    ): GetLikesResponse {
        val url = "https://api.twitter.com/2/users/$userId/liked_tweets"
        val response = httpClient.request(url) {
            method = HttpMethod.Get
            headers {
                bearerAuth(accessToken)
            }
            url {
                parameters.append(
                    "expansions",
                    getLikesRequest.expansions.joinToString(separator = ",")
                )
                parameters.append(
                    "media.fields",
                    getLikesRequest.mediaFields.joinToString(separator = ",")
                )
                getLikesRequest.paginationToken?.let {
                    parameters.append("pagination_token", it)
                }
            }
        }
        val getLikesResponse = response.body<JsonElement>()
        return GetLikesResponseParser().parse(getLikesResponse.jsonObject)
    }
}

class GetLikesResponseParser {

    fun parse(response: JsonObject): GetLikesResponse {
        val nextToken =
            requireNotNull(requireNotNull(response["meta"]).jsonObject["next_token"]).jsonPrimitive.content
        val tweetsList = requireNotNull(response["data"]).jsonArray
        val mediaArray =
            requireNotNull(requireNotNull(response["includes"]).jsonObject["media"]).jsonArray
        val mediaList = mediaArray.asSequence()
        val tweets = tweetsList.map { tweet ->
            val tweetObject = tweet.jsonObject
            val keys =
                tweetObject["attachments"]?.jsonObject?.get("media_keys")?.jsonArray?.map { it.jsonPrimitive.content }
                    ?: emptyList()
            val photos = if (keys.isEmpty()) emptyList() else mediaList.filter {
                mediaInThis(
                    keys,
                    it,
                    "photo"
                )
            }.map { toPhoto(it) }.toList()
            val videos = if (keys.isEmpty()) emptyList() else mediaList.filter {
                mediaInThis(
                    keys,
                    it,
                    "video"
                )
            }.map { toVideo(it) }.toList()
            Tweet(
                id = requireNotNull(tweetObject["id"]).jsonPrimitive.content,
                text = requireNotNull(tweetObject["text"]).jsonPrimitive.content,
                photos = photos,
                videos = videos,
            )
        }
        return GetLikesResponse(
            tweets = tweets,
            nextToken = nextToken,
        )
    }

    private fun mediaInThis(keys: List<String>, media: JsonElement, type: String): Boolean {
        return keys.contains(requireNotNull(media.jsonObject["media_key"]).jsonPrimitive.content) &&
                requireNotNull(media.jsonObject["type"]).jsonPrimitive.content == type
    }

    private fun toPhoto(media: JsonElement): Photo {
        return Photo(
            mediaKey = requireNotNull(media.jsonObject["media_key"]).jsonPrimitive.content,
            url = requireNotNull(media.jsonObject["url"]).jsonPrimitive.content,
        )
    }

    private fun toVideo(media: JsonElement): Video {
        return Video(
            mediaKey = requireNotNull(media.jsonObject["media_key"]).jsonPrimitive.content,
            previewImageUrl = requireNotNull(media.jsonObject["preview_image_url"]).jsonPrimitive.content,
            variants = requireNotNull(media.jsonObject["variants"]).jsonArray.map {
                VideoVariant(
                    contentType = requireNotNull(it.jsonObject["content_type"]).jsonPrimitive.content,
                    url = requireNotNull(it.jsonObject["url"]).jsonPrimitive.content,
                    bitRate = it.jsonObject["bit_rate"]?.jsonPrimitive?.content?.toInt()
                )
            }
        )
    }
}

@Serializable
data class GetLikesRequest(
    val expansions: Set<String> = setOf("attachments.media_keys"),
    val mediaFields: Set<String> = setOf(
        "media_key",
        "preview_image_url",
        "type",
        "url",
        "variants"
    ),
    val paginationToken: String? = null,
)

data class GetLikesResponse(
    val tweets: List<Tweet>,
    val nextToken: String,
)

data class Tweet(
    val id: String,
    val text: String,
    val photos: List<Photo>,
    val videos: List<Video>,
)

interface Media {
    val mediaKey: String
}

data class Video(
    override val mediaKey: String,
    val variants: List<VideoVariant>,
    val previewImageUrl: String,
) : Media

data class VideoVariant(
    val contentType: String,
    val url: String,
    val bitRate: Int?
)

data class Photo(
    override val mediaKey: String,
    val url: String,
) : Media


@Serializable
data class Attachment(
    val media_keys: List<String>
)


@Serializable
data class AuthResponse(
    val token_type: String = "",
    val access_token: String = "",
)
