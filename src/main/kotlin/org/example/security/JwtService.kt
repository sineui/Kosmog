package org.example.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import org.example.ExampleProperties
import org.example.model.security.JwtUser
import org.example.model.security.TokenPayload
import org.example.model.user.User
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(private val properties: ExampleProperties) {
    private val encodedSecret = Base64.getEncoder().encodeToString(properties.jwt.secret.toByteArray())
    private val encodedRefreshTokenSecret =
        Base64.getEncoder().encodeToString(properties.jwt.refreshToken.secret.toByteArray())
    private val encodedPasswordResetTokenSecret =
        Base64.getEncoder().encodeToString(properties.jwt.passwordResetToken.secret.toByteArray())

    fun extractTokenFrom(request: ServerHttpRequest) = extractTokenFrom(request.headers)
    fun extractTokenFrom(headers: HttpHeaders): String? {
        val authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return null
        return if (authHeader.startsWith(properties.jwt.prefix)) {
            authHeader.substring(properties.jwt.prefix.length)
        } else {
            null
        }
    }

    fun getAllClaimsFrom(token: String, signingKey: String): Claims {
        return Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(token)
            .body
    }

    fun getPasswordResetUserIdFrom(token: String): String {
        val claims = getAllClaimsFrom(token, encodedPasswordResetTokenSecret)
        return claims["id"] as String
    }

    fun getJwtUserFrom(token: String): JwtUser {
        val claims = try {
            getAllClaimsFrom(token, encodedSecret)
        } catch (e: SignatureException) {
            getAllClaimsFrom(token, encodedRefreshTokenSecret)
        } catch (e: Exception) {
            throw e
        }

        return JwtUser(
            id = claims["id"] as String,
            email = claims["email"] as String,
            name = claims["name"] as String?,
            role = claims["role"] as String
        )
    }

    fun generatePayload(user: User): TokenPayload {
        val jwtUser = JwtUser(user)
        return TokenPayload(token = generateToken(jwtUser))
    }

    fun generateToken(jwtUser: JwtUser): String {
        return generateToken(properties.jwt.duration, jwtUser.claims, encodedSecret)
    }

    fun generateRefreshToken(jwtUser: JwtUser): String {
        return generateToken(properties.jwt.refreshToken.duration, jwtUser.claims, encodedRefreshTokenSecret)
    }

    fun generatePasswordResetToken(userId: String): String {
        return generateToken(
            properties.jwt.passwordResetToken.duration,
            hashMapOf("id" to userId) as Map<String, Any>,
            encodedPasswordResetTokenSecret
        )
    }

    private fun generateToken(expirationDuration: Long, claims: Map<String, Any?>, encodedSecret: String): String {
        val createdDate = Date()
        val expirationDate = Date(createdDate.time + expirationDuration)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(createdDate)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, encodedSecret)
            .compact()
    }
}
