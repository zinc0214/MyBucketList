package womenproject.com.mybury.data.model

import kotlinx.serialization.Serializable

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
data class DomainUseUserIdRequest(
    val userId: String
)

@Serializable
data class DomainTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val retcode: String
)