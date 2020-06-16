package org.example.datafetchers

import org.example.datafetchers.feature.HasUser
import org.example.repositories.UserRepository
import org.springframework.stereotype.Component

@Component
class UserDataFetchers(override val userRepository: UserRepository) : HasUser {

    fun currentUser() = monoDataFetcher {
        currentUser(it)
    }
}
