package com

import io.ktor.server.netty.*
import com.plugins.*
import io.ktor.application.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureSerialization()
}
