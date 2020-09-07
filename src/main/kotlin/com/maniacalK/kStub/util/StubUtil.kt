package com.maniacalK.kStub.util

import com.google.gson.Gson
import com.maniacalK.kStub.STUB_PATH
import com.maniacalK.kStub.model.StubItem
import io.ktor.http.*
import org.slf4j.LoggerFactory
import java.io.File

class StubUtil {

    private val gson = Gson()
    private val logger = LoggerFactory.getLogger("kStub")

    companion object {
        const val MAC_DS_STORE = ".DS_Store"
    }

    fun getRoutes(): List<StubItem> {
        return File(STUB_PATH).walk().filterNot { it.path.contains(MAC_DS_STORE) }.mapNotNull { file ->
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

    fun loadBody(fileName: String, parameters: Parameters): String {
        var output = File(fileName).readText(Charsets.UTF_8)
        parameters.entries().forEach {
            output = output.replace("{${it.key}}", it.value.first(), true)
        }
        return output
    }
}