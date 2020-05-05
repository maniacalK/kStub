package com.kStub.stubber

import com.google.gson.Gson
import com.kStub.model.StubItem
import org.slf4j.LoggerFactory
import java.io.File

class Stubber {

    private val gson = Gson()
    val logger = LoggerFactory.getLogger("main")

    fun getRoutes(): List<StubItem> {
        return File("stub/login").walk().mapNotNull { file ->
            if (!file.isDirectory) {
                val content = File(file.path).readText(Charsets.UTF_8)
                gson.fromJson(content, StubItem::class.java).also {
                    logger.info("Found ${file.path}")
                }
            } else null
        }.toList()
    }

    fun loadBody(fileName: String): String {
        return File(fileName).readText(Charsets.UTF_8)
    }
}