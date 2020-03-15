package com.kotlin.bootstrap.security.entity

import javax.persistence.*

@Entity
@Table(name = "roles", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "role"])])
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,

        @ManyToOne(fetch = FetchType.LAZY)
        var user: User?,

        var role: String
) {
    constructor(role: String, user: User?) : this(
            id = null, role = role, user = user
    )
}