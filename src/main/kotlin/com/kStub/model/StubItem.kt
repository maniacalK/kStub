package com.kStub.model

data class StubItem(
        val request: StubRequest,
        val response: StubResponse)

data class StubRequest(
        val method: String,
        val url: String)

data class StubResponse(
        val status: Int,
        val body: String,
        val bodyFile: String? = null,
        val headers: HashMap<String, String>)
