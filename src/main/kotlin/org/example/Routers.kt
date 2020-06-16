package org.example

import org.example.handlers.HandleGraphQLRequest
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.router

@Service
class Routers(
    private val handleGraphQLRequest: HandleGraphQLRequest
) {
    @Bean
    fun routes() = router {
        (GET("/graphql") or POST("/graphql")) { handleGraphQLRequest(it) }
    }
}
