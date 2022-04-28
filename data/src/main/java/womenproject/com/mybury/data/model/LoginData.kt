package womenproject.com.mybury.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserIdRequest(
    val userId: String
)

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val retcode: String
)

@Serializable
data class SignUpCheckRequest(
    var email: String
)

@Serializable
data class SignUpCheckResponse(
    val signUp: Boolean,
    val userId: String
)

@Serializable
data class SignUpResponse(
    val retcode: String,
    val userId: String
)

@Serializable
data class UseUserIdRequest(
    val userId: String
)

@Serializable
data class GetTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val retcode: String
)