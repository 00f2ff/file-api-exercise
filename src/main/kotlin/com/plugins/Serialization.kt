package com.plugins

import com.Failure
import com.FileService.queryFileSystem
import com.Success
import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.nio.file.Files
import java.nio.file.Path

fun Application.configureSerialization() { // todo: worth changing up the plugins a bit?
    install(ContentNegotiation) {
        json()
    }
    val basePath = environment.config.propertyOrNull("ktor.base_path")?.getString()
    if (basePath.isNullOrBlank() || !Files.exists(Path.of(basePath))) throw RuntimeException("Invalid base path")

    routing {
        get("/{...}") {
            // fixme: pull in this data via config
            val basePath2 = Path.of("/Users/duncan/workspaces/interview_2021/weavegrid/src/main/resources/testDir")
            val result = queryFileSystem(basePath2, call.request)
            when (result) {
                is Success -> call.respond(Json.encodeToJsonElement(result.data))
                is Failure -> call.response.status(HttpStatusCode(404, result.error.value))
            }
        }
    }
}

/*
todo: do I want to make a recursive flag?
todo: get rid of type in serialization
 */
