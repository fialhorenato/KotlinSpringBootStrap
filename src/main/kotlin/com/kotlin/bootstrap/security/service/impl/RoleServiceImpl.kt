package com.kotlin.bootstrap.security.service.impl

import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.repository.RoleRepository
import com.kotlin.bootstrap.security.service.RoleService
import com.kotlin.bootstrap.security.service.UserService
import com.kotlin.bootstrap.security.service.impl.UserServiceImpl
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(private val roleRepository: RoleRepository, private val userService: UserService) : RoleService {

    override fun addRoleByEmail(email : String, role : String): Role {
        val user = userService.findByEmail(email)
        val role = Role(role = role, user = user)
        return roleRepository.save(role)
    }

    override fun removeRoleByEmail(email : String, role : String) {
        roleRepository.deleteByUser_EmailAndRole(email, role)
    }
}