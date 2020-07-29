package com.kotlin.bootstrap.security.entity

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,

        @Size(min = 1, max = 100)
        var username: String,

        @Email
        var email: String,

        var password: String,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
        var roles: List<Role>
) {
        @PrePersist
        fun setRoles() {
                roles.forEach { it.user = this }
        }
}