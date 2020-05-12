package com.kStub

import com.google.gson.Gson
import com.kStub.stubber.Stubber
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.NotFoundException
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.response.respondFile
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.io.File

const val STUB_PATH = "stub"
val logger = LoggerFactory.getLogger("app")
val stubber = Stubber()

fun Application.module() {
    install(StatusPages) {
        exception<NotFoundException> {
            call.respondText("NOT FOUND")
        }
    }
    install(ContentNegotiation) {
        gson() {
            setPrettyPrinting()
            serializeNulls()
            setLenient()

        }
    }
    install(DefaultHeaders)
    install(CallLogging) {
        level = Level.TRACE
    }
    install(Routing) {

        val logger = LoggerFactory.getLogger("main")

        stubber.getRoutes().forEach { item ->
            logger.info(item.toString())
            when (item.request.method) {
                "GET" -> get(item.request.url) {
                    logger.info("Stub Hit: [${item.request.method}] ${item.request.url}")
                    item.response.bodyFile?.let {
                        call.respondFile(File("$STUB_PATH/$it"))
                    } ?: call.respondText(item.response.body)
                }
                else -> {
                    println("Invalid Method")
                }
            }
        }
    }
}

class App {
    companion object {
        fun main(args: Array<String>) {
            embeddedServer(Netty, 8080,
                    watchPaths = listOf("App.kt"),
                    module = Application::module).start(wait = true)
        }
    }
}

fun main(args: Array<String>) {
    val config = stubber.loadConfig("config/kstub_config.json")
    val options = getOptions(args)

    logger.info(Gson().toJson(config))
    logger.info(options.toString())

    embeddedServer(
            factory = Netty,
            port = options["port"]?.toInt() ?: config.port,
            watchPaths = listOf("App.kt"),
            module = Application::module
    ).start(wait = true)
}

private fun getOptions(args: Array<String>): Map<String, String> {
    return args.mapNotNull {
        val splits = it.split("=")
        if (splits.size == 2 && splits[0].startsWith("--")) {
            splits[0].removePrefix("--").toLowerCase() to splits[1]
        } else null
    }.toList().toMap()
}
