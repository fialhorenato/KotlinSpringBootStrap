package com.kotlin.bootstrap.security.entity

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.Size
import kotlin.math.min

@Entity
@Table(name = "users")
data class User (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id : Long?,

        @Size(min = 1, max = 100)
        var username : String,

        @Email
        var email : String,

        var password : String,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.EAGER)
        var roles : List<Role>
)