package org.example.model.security

import org.example.model.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtUser(
    val id: String,
    val email: String,
    val name: String?,
    val role: String
) : UserDetails {
    private val authorities = listOf<GrantedAuthority>(SimpleGrantedAuthority(role))

    constructor(user: User) : this(user.id.toString(), user.email, user.name, user.role)

    val claims = mutableMapOf(
        "id" to id,
        "email" to email,
        "name" to name,
        "role" to role
    )

    override fun getAuthorities() = authorities
    override fun isEnabled() = true
    override fun getUsername() = email
    override fun isCredentialsNonExpired() = true
    override fun getPassword() = "NO_PASSWORD"
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
}
