package org.featx.sta2ry.haldus

import com.google.inject.Injector
import io.vertx.core.Promise
import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import java.util.concurrent.Callable

class GuiceVerticleFactory(private val injector: Injector) : VerticleFactory {
    override fun prefix(): String {
        return "org.featx.sta2ry.haldus"
    }

    override fun createVerticle(verticleName: String, classLoader: ClassLoader, promise: Promise<Callable<Verticle>>) {
        val verticle = VerticleFactory.removePrefix(verticleName)
        promise.complete { injector.getInstance(classLoader.loadClass(verticle)) as Verticle }
    }
}