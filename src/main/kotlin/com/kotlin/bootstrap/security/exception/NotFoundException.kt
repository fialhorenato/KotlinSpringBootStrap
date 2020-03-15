package com.kotlin.bootstrap.security.exception

import java.util.function.Supplier

class NotFoundException : RuntimeException(), Supplier<Throwable> {
    override fun get(): Throwable {
        TODO("Not yet implemented")
    }

}
