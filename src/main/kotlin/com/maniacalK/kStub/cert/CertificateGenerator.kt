package com.maniacalK.kStub.cert

import io.ktor.network.tls.certificates.*
import org.slf4j.LoggerFactory
import java.io.File

object CertificateGenerator {

    private val logger = LoggerFactory.getLogger("certGenerator")

    @JvmStatic
    fun main(args: Array<String>) {
        val jksFile = File("certs/kStub.jks").apply {
            parentFile.mkdirs()
        }

        if (!jksFile.exists()) {
            logger.info("Generating certificate")
            generateCertificate(jksFile)
        }
    }
}