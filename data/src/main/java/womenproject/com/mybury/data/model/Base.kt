package womenproject.com.mybury.data.model

import kotlinx.serialization.Serializable

enum class LoadState {
    START, RESTART, SUCCESS, FAIL
}

@Serializable
data class RefreshTokenRequest(
    val userId: String,
    val refreshToken: String
)

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val retcode: String
)

enum class BucketType {
    SUCCEED_ITEM, COUNT_ITEM, BASE_ITEM;

    fun int(): Int =
        when (this) {
            SUCCEED_ITEM -> 0
            COUNT_ITEM -> 1
            BASE_ITEM -> 2
        }
}