package org.featx.sta2ry.haldus

import com.google.inject.Guice
import com.google.inject.Stage
import org.featx.sta2ry.haldus.enums.EventBusChannels
import org.featx.sta2ry.haldus.handler.UserHandler
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.core.*
import io.vertx.ext.web.Router
import io.vertx.kotlin.config.configRetrieverOptionsOf
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import javax.inject.Inject
import kotlin.system.exitProcess

class MainVerticle : AbstractVerticle() {

  @Inject
  private lateinit var userHandler: UserHandler

  override fun start(startPromise: Promise<Void>) {

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8888) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(http.cause());
        }
      }
  }
}


fun main(args: Array<String>) {
  val vertx = Vertx.vertx()
  val configRetrieverOptions = getConfigRetrieverOptions()
  val configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions)

  // getConfig is called for initial loading
  configRetriever.getConfig { ar ->
    if (ar.failed()) {
      println("Load config error" + ar.cause().message)
      exitProcess(-1)
    }
    val config = ar.result()

    val instances = Runtime.getRuntime().availableProcessors()
    val deploymentOptions = DeploymentOptions().setInstances(instances).setConfig(config)

    val injector = Guice.createInjector(Stage.PRODUCTION, AppContext(vertx))
    vertx.registerVerticleFactory(GuiceVerticleFactory(injector))

    vertx.deployVerticle("org.featx.cusp.user:" + MainVerticle::class.java.name, deploymentOptions)
  }
  // listen is called each time configuration changes
  configRetriever.listen { change ->
    val updatedConfiguration = change.getNewConfiguration()
    vertx.eventBus().publish(EventBusChannels.CONFIGURATION_CHANGED.name, updatedConfiguration)
  }
}

fun getConfigRetrieverOptions(): ConfigRetrieverOptions {
  val fileStore = configStoreOptionsOf(
    type = "file",
    format = "properties",
    config = json {
      obj("path" to "application.properties")
    })

  val sysPropsStore = configStoreOptionsOf(
    type = "sys",
    optional = true
  )

  val httpStore = configStoreOptionsOf(
    type = "http",
    format = "json",
    optional = true,
    config = json {
      obj(
        "host" to "127.0.0.1",
        "port" to 8500,
        "path" to "/v1/kv/v1/kv/config/ubs,dev/data"
      )
    })
  return configRetrieverOptionsOf(
    includeDefaultStores = true,
    //Tested, priority raised as order.
    stores = listOf(sysPropsStore, httpStore, fileStore),
    scanPeriod = 5000
  )
}