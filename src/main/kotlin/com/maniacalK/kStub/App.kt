package com.maniacalK.kStub

import com.google.gson.Gson
import com.maniacalK.kStub.templates.MainTemplate
import com.maniacalK.kStub.templates.RouteTemplate
import com.maniacalK.kStub.util.StubUtil
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.NotFoundException
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.html.respondHtml
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.defaultForFilePath
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h2
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

const val STUB_PATH = "stub"
val logger = LoggerFactory.getLogger("kStub")
val stubUtil = StubUtil()

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
        get("/admin/test") {
            call.respondHtmlTemplate(RouteTemplate("AdminTest")) {
                name {
                    +"HelloName"
                }
                request {
                    +"Req"
                }
                response {
                    +"Resp"
                }
            }
        }

        stubUtil.getRoutes().forEach { item ->
            get("/admin/route/${item.id}") {
                call.respondHtmlTemplate(RouteTemplate(
                        "kStub Route - ${item.linkName}",
                        "Route: ${item.linkName}"
                )) {
                    name {
                        +item.linkName
                    }
                    request {
                        +item.request.toString()
                    }

                    response {
                        +item.response.toString()
                    }
                }
            }
        }

        get("/admin") {
            call.respondHtmlTemplate(MainTemplate("kStub Admin")) {
                content {
                    h2 { +"Routes" }
                    div(classes = "row") {
                        div(classes = "col-sm-8") {
                            div(classes = "list-group") {
                                stubUtil.getRoutes().forEach { item ->
                                    a("/admin/route/${item.id}", classes = "list-group-item") {
                                        +item.linkName
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        logger.info("Stubs:")
        stubUtil.getRoutes().forEach { item ->
            logger.info(item.toString())
            when (item.request.method) {
                "GET" -> get(item.request.url) {
                    logger.info("Stub Hit: [${item.request.method}] ${item.request.url}, params: ${call.parameters.entries()}, headers: [${getHeaders(call.request.headers)}]")
                    item.response.bodyFile?.let { bodyPath ->
                        call.respondText(
                                text = stubUtil.loadBody("$STUB_PATH/$bodyPath", call.parameters),
                                contentType = item.response.headers["Content-Type"]?.let { ContentType.parse(it) } ?: ContentType.defaultForFilePath("$STUB_PATH/$bodyPath"))
                    } ?: call.respondText(item.response.body)
                }
                else -> {
                    println("Invalid Method")
                }
            }
        }
    }
}

private fun getHeaders(headers: Headers): String {
    return headers.entries().joinToString(", ") { "\"${it.key}: ${it.value.first()}\"" }
}

fun main(args: Array<String>) {
    val config = stubUtil.loadConfig("config/kstub_config.json")
    val options = getOptions(args)

    logger.info(Gson().toJson(config))
    logger.info(options.toString())

    embeddedServer(
            host = options["host"] ?: config.host,
            factory = Netty,
            port = options["port"]?.toInt() ?: config.port,
            watchPaths = listOf("maniacalK/kStub"),
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