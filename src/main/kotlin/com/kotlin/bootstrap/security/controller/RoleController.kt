package com.kotlin.bootstrap.security.controller

import com.kotlin.bootstrap.security.dto.role.RoleRequestDTO
import com.kotlin.bootstrap.security.service.RoleService
import com.kotlin.bootstrap.security.service.impl.RoleServiceImpl
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/role")
class RoleController(private val roleService: RoleService) {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    fun add(@RequestBody roleRequestDTO: RoleRequestDTO) {
        roleService.addRoleByEmail(email = roleRequestDTO.email, role = roleRequestDTO.role)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    fun delete(@RequestBody roleRequestDTO: RoleRequestDTO) {
        roleService.removeRoleByEmail(email = roleRequestDTO.email, role = roleRequestDTO.role)
    }
}