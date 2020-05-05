/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.kStub

import com.kStub.stubber.Stubber
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level

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

        val stubber = Stubber()


        stubber.getRoutes().forEach { item ->
            when (item.request.method) {
                "GET" -> get(item.request.url) {
                    call.respond(
                            item.response.bodyFile?.let {
                                stubber.loadBody("body/$it")
                            } ?: item.response.body
                    )
                }
                else -> null
            }
        }

        get("/") {
            call.respondText("{  }", ContentType.Text.JavaScript)
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080,
            watchPaths = listOf("App.kt"),
            module = Application::module).start(wait = true)
}
