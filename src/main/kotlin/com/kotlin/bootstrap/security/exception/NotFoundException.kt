package com.kotlin.bootstrap.security.exception

import java.util.function.Supplier

class NotFoundException(override val message : String) : RuntimeException(), Supplier<Throwable> {
    override fun get(): Throwable {
        throw NotFoundException(message)
    }
}
