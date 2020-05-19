package org.example.repositories

import org.example.model.user.User
import org.springframework.stereotype.Repository

@Repository
class UserRepository : AbstractRepository<User>("users", User::class.java) {
}