package org.example.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtSecurityContextRepository(private val jwtService: JwtService) : ServerSecurityContextRepository {
    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        throw UnsupportedOperationException("Server is Stateless")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val token = jwtService.extractTokenFrom(exchange.request) ?: return Mono.empty()
        val user = jwtService.getJwtUserFrom(token)

        return Mono.just(SecurityContextImpl(UsernamePasswordAuthenticationToken(user, token, user.authorities)))
    }
}
