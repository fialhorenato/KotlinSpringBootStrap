package com.kotlin.bootstrap.security.service

import com.kotlin.bootstrap.security.entity.Role

interface RoleService {
    fun addRoleByEmail(email : String, role : String): Role
    fun removeRoleByEmail(email : String, role : String)
}
