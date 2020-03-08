package com.kotlin.bootstrap.security.entity

import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,

        @ManyToOne(fetch = FetchType.LAZY)
        var user: User?,

        var role: String
)