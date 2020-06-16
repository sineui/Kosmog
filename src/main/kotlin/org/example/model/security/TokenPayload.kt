package org.example.model.security

data class TokenPayload(
    val token: String? = null,
    val message: String? = null
)
