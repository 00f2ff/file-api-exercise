package com.plugin

import com.Failure
import com.FileService.queryFileSystem
import com.Success
import io.github.cdimascio.dotenv.dotenv
import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

/**
 * Provides the application with serialization instructions and a corresponding route.
 * Also finds the base path set in an environment variable, and performs validation on it.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    val dotenv = dotenv()
    val basePath = dotenv["BASE_PATH"]
    logger.info { basePath }
    if (basePath.isNullOrBlank() || !Files.exists(Path.of(basePath))) throw RuntimeException("Invalid base path")

    routing {
        get("/{...}") {
            val result = queryFileSystem(Path.of(basePath), call.request)
            when (result) {
                is Success -> {
                    logger.info { "Found file or directory successfully" }
                    call.respond(Json.encodeToJsonElement(result.data))
                }
                is Failure -> {
                    logger.error { "Could not find file or directory" }
                    call.response.status(HttpStatusCode(404, result.error.value))
                }
            }
        }
    }
}