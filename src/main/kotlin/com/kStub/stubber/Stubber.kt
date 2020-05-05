package com.kStub.stubber

import com.google.gson.Gson
import com.kStub.model.StubItem
import java.io.File

class Stubber {

    private val gson = Gson()

    fun getRoutes(): List<StubItem> {
        return File("stub/login").walk().mapNotNull {
            if (!it.isDirectory) {
                println(it.path)
                val content = File(it.path).readText(Charsets.UTF_8)
                gson.fromJson(content, StubItem::class.java)
            } else null
        }.toList()
    }

    fun loadBody(fileName: String): String {
        return File(fileName).readText(Charsets.UTF_8)
    }
}