package com

import com.plugin.configureSerialization
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.call
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
//import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.EngineMain
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import mu.KotlinLogging
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureSerialization()
}
