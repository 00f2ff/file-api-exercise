package com

import com.plugin.configureSerialization
import io.ktor.application.Application
import io.ktor.server.netty.EngineMain
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureSerialization()
}
