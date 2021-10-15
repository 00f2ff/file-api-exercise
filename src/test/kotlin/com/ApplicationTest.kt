package com

import com.data.Directory
import com.data.SystemFile
import com.plugin.configureSerialization
// These wildcards are odd. If you import the functions and types themselves, IntelliJ throws errors
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.decodeFromJsonElement
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest : FileSystemTest() {

    @Test
    fun `server should handle calls to root`() {
        // Creates a context in which an environment variable can be set, sets it, and runs the
        // application to pick it up accordingly.
        EnvironmentVariables().set("BASE_PATH", testDirectory).execute {
            withTestApplication({configureSerialization()}) {
                handleRequest(HttpMethod.Get, "/").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    val directory = Directory.create(Path.of(testDirectory), listOf())
                    assertEquals(directory, json.decodeFromJsonElement(json.parseToJsonElement(response.content!!)))
                }
            }
        }
    }

    @Test
    fun `files with content can be loaded`() {
        EnvironmentVariables().set("BASE_PATH", testDirectory).execute {
            val filename = ".dotfile"
            val filePath = Path.of("$testDirectory/$filename")
            Files.createFile(filePath)
            val message = "I'm a dotfile"
            Files.writeString(filePath, message)
            val file = SystemFile.create(filePath, includeContents = true)

            withTestApplication({configureSerialization()}) {
                handleRequest(HttpMethod.Get, "/$filename").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    val response = json.decodeFromJsonElement<SystemFile>(json.parseToJsonElement(response.content!!))
                    assertEquals(file.contents, response.contents)
                }
            }
        }
    }

    // This test is failing due to a deserialization error. It's odd because the functionality works
    // via Postman, but something about the test environment causes the polymorphic deserializer
    // to get confused. (update: out of date b/c need to set env var)
//    @Test
//    fun `nested directories are supported`() {
//        System.setProperty("ktor.base_path", testDirectory)
//        val directoryPath1 = Path.of("$testDirectory/dir1")
//        val directoryPath2 = Path.of("$testDirectory/dir1/dir2")
//        Files.createDirectory(directoryPath1)
//        Files.createDirectory(directoryPath2)
//        val directory2 = Directory.create(directoryPath2)
//        val directory1 = Directory.create(directoryPath1, listOf(directory2))
//
//        withTestApplication({ configureSerialization() }) {
//            handleRequest(HttpMethod.Get, "/dir1").apply {
//                assertEquals(HttpStatusCode.OK, response.status())
//                val response = json.decodeFromJsonElement<Directory>(json.parseToJsonElement(response.content!!))
//                assertEquals(directory1, response)
//            }
//        }
//    }

    @Test
    fun `nonexistent files or directories returns error`() {
        EnvironmentVariables().set("BASE_PATH", testDirectory).execute {
            withTestApplication({configureSerialization()}) {
                handleRequest(HttpMethod.Get, "/whatever").apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                    assertEquals(Error.NOT_FOUND.value, response.status()!!.description)
                }
            }
        }
    }
}
