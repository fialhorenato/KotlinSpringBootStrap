package com.kotlin.bootstrap.security.service

import com.kotlin.bootstrap.security.entity.User

interface UserService {
    fun findByEmail(email : String) : User
    fun create(username : String, password : String, email : String): User
    fun me(): User
    fun deleteById(id : Long)
    fun deleteByEmail(email : String)
}
