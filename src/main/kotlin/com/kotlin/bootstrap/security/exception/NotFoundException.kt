package com.kotlin.bootstrap.security.exception

import java.lang.RuntimeException
import java.util.function.Supplier

class NotFoundException : RuntimeException(), Supplier<Throwable> {
    override fun get(): Throwable {
        TODO("Not yet implemented")
    }

}
