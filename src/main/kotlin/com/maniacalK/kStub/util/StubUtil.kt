package com.maniacalK.kStub.util

import com.google.gson.Gson
import com.maniacalK.kStub.model.StubConfig
import com.maniacalK.kStub.model.StubItem
import org.slf4j.LoggerFactory
import java.io.File

class StubUtil {

    private val gson = Gson()
    private val logger = LoggerFactory.getLogger("stubber")

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

    fun loadConfig(filePath: String?): StubConfig =
            filePath?.let {
                File(it).run {
                    if (exists()) {
                        Gson().fromJson<StubConfig>(
                                readText(Charsets.UTF_8),
                                StubConfig::class.java
                        )
                    } else {
                        logger.info("No config file found: Using Default Config")
                        StubConfig()
                    }
                }
            } ?: StubConfig()

}