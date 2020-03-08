package com.kotlin.bootstrap.security.repository

import com.kotlin.bootstrap.security.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username : String) : User?

    fun existsByUsernameOrEmail(username : String, email : String) : Boolean
}