package org.example

import org.example.security.JwtSecurityContextRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class ExampleSecurityConfiguration(private val jwtSecurityContextRepository: JwtSecurityContextRepository) {
    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        // @formatter:off
        return http
            .csrf()
                .disable()
            .cors()
                .configurationSource(corsConfigurationSource())
                .and()
            .httpBasic()
                .disable()
            .formLogin()
                .disable()
            .securityContextRepository(jwtSecurityContextRepository)
            .build()
        // @formatter:on
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfiguration())
        }
    }

    private fun corsConfiguration(): CorsConfiguration {
        return CorsConfiguration().apply {
            // keep the order
            allowedOrigins = listOf("https://example.org", "http://localhost:3000")
            allowCredentials = true
            applyPermitDefaultValues() // includes GET, POST.
            addAllowedMethod(HttpMethod.PUT)
            addAllowedMethod(HttpMethod.PATCH)
            addAllowedMethod(HttpMethod.DELETE)
        }
    }
}
