package com.kStub.stubber

import com.google.gson.Gson
import com.kStub.model.StubItem
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.Exception

class Stubber {

    private val gson = Gson()
    val logger = LoggerFactory.getLogger("main")

    fun getRoutes(): List<StubItem> {
        return File("stub").walk().mapNotNull { file ->
            if (!file.isDirectory && !file.path.contains("body")) {
                val content = File(file.path).readText(Charsets.UTF_8)
                try {
                    gson.fromJson(content, StubItem::class.java).also {
                        logger.debug("Found ${file.path}")
                    }
                } catch (e: Exception) {
                    logger.warn("Invalid Stub Spec: ${file.path}")
                    null
                }
            } else null
        }.toList()
    }

    fun loadBody(fileName: String): String {
        return File(fileName).readText(Charsets.UTF_8)
    }
}