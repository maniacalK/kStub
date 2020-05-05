package com.kStub.model

data class StubItem(
        val request: StubRequest,
        val response: StubResponse) {

    override fun toString(): String {
        return "Stub: [\n\t$request\n\t$response\n]"
    }
}

data class StubRequest(
        val method: String,
        val url: String) {
    override fun toString(): String {
        return "Request: method($method), url($url)"
    }
}

data class StubResponse(
        val status: Int,
        val body: String,
        val bodyFile: String? = null,
        val headers: HashMap<String, String>) {

    override fun toString(): String {
        val headersString = headers.map { "${it.key}:${it.value}" }
                .joinToString(", ")
        return "Response: status($status) " +
                "bodyFile($bodyFile) " +
                "headers($headersString)"
    }
}
