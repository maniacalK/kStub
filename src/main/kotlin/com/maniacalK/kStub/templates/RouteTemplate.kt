package com.maniacalK.kStub.templates

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.HTML
import kotlinx.html.HtmlBlockTag
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.onClick
import kotlinx.html.section

class RouteTemplate(pageTitle: String, pageHeader: String? = null) : Template<HTML> {
    val main: MainTemplate = MainTemplate(pageTitle, pageHeader)
    val name = Placeholder<HtmlBlockTag>()
    val request = Placeholder<HtmlBlockTag>()
    val response = Placeholder<HtmlBlockTag>()
    val routeContent = Placeholder<HtmlBlockTag>()

    override fun HTML.apply() {
        insert(main) {
            content {
                section {
                    div(classes = "row") {
                        div(classes = "col-sm-4") {
                            div(classes = "panel panel-primary") {
                                div(classes = "panel-heading") {
                                    +"Name"
                                }
                                div(classes = "panel-body") {
                                    insert(name)
                                }
                            }
                        }
                    }
                    div(classes = "row") {
                        div(classes = "col-sm-8") {
                            div(classes = "panel panel-default") {
                                div(classes = "panel-heading") {
                                    +"Request"
                                }
                                div(classes = "panel-body") {
                                    insert(request)
                                }
                            }
                        }
                    }
                    div(classes = "row") {
                        div(classes = "col-sm-8") {
                            div(classes = "panel panel-default") {
                                div(classes = "panel-heading") {
                                    +"Response"
                                }
                                div(classes = "panel-body") {
                                    insert(response)
                                }
                            }
                        }
                    }
                }

                section {
                    div(classes = "row") {
                        div(classes = "col-sm-8") {
                            button(classes = "btn btn-default") {
                                onClick = "window.location.href = '/admin'"
                                +"back"
                            }
                        }
                    }
                }

                insert(routeContent)
            }
        }
    }
}