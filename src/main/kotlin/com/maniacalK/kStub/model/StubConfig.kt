package com.maniacalK.kStub.model

data class StubConfig(
        val port: Int = DEFAULT_PORT,
        val host: String = DEFAULT_HOST
) {
    companion object {
        const val DEFAULT_PORT = 8080
        const val DEFAULT_HOST = "127.0.0.1"
    }
}

