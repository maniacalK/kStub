package com.maniacalK.kStub.model

data class StubItem(
        val name: String,
        val group: String? = null,
        val request: StubRequest,
        val response: StubResponse) {

    override fun toString(): String {
        return "$name${groupToString()}: [$request, $response]"
    }

    val id: String
        get() = "${request.method.toLowerCase()}-$name"

    val linkName: String
        get() = "$name${groupToString()}"

    private fun groupToString(): String? = if (!group.isNullOrBlank()) " ($group)" else ""

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
