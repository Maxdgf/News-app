package com.maxdgf.newspaper.domain.model.reponse

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: String,
    val code: String,
    val message: String
)
