package com.kotlin.bootstrap.security.repository

import com.kotlin.bootstrap.security.entity.Role
import com.kotlin.bootstrap.security.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, Long>