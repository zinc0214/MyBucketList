package com.zinc.data.model

data class SignUpCheckRequest(
    var email: String
)

data class SignUpCheckResponse(
    val signUp: Boolean,
    val userId: String
)

data class SignUpResponse(
    val retcode: String,
    val userId: String
)