package com.zinc.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SimpleResponse(
    val retcode: String
)