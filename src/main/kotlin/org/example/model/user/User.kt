package org.example.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import org.example.model.MongoDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    val email: String,
    val name: String,
    @JsonIgnore
    val encryptedPassword: String
) : MongoDocument()