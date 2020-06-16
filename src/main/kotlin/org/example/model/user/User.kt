package org.example.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import org.example.model.MongoDocument

data class User(
    val email: String,
    val name: String,
    @JsonIgnore
    val encryptedPassword: String,
    val role: String
) : MongoDocument()
