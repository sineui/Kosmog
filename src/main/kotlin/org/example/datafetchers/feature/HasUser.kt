package org.example.datafetchers.feature

import graphql.schema.DataFetchingEnvironment
import org.bson.types.ObjectId
import org.example.model.security.JwtUser
import org.example.model.user.User
import org.example.repositories.UserRepository
import org.springframework.security.core.context.SecurityContext
import reactor.core.publisher.Mono
import reactor.util.context.Context

interface HasUser {
    val userRepository: UserRepository

    fun jwtUser(environment: DataFetchingEnvironment): Mono<JwtUser> {
        return securityContext(environment.getContext())
            .map { it.authentication.principal }
            .cast(JwtUser::class.java)
    }

    fun currentUser(environment: DataFetchingEnvironment): Mono<User> {
        return jwtUser(environment)
            .flatMap { userRepository.findById(ObjectId(it.id)) }
    }
}

private fun securityContext(context: Context): Mono<SecurityContext> {
    return if (context.hasKey(SecurityContext::class.java)) {
        context.get<Mono<SecurityContext>>(SecurityContext::class.java)
    } else {
        Mono.empty()
    }
}
