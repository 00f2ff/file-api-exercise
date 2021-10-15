package com.config

import com.FileSystemTest
import com.data.Directory
import com.data.SystemFile
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class WeaveDataSerializerTest : FileSystemTest() {

    @Test
    fun `polymorphic deserialization works`() {
        val filePath = Path.of("$testDirectory/.dotfile")
        Files.createFile(filePath)

        val file = SystemFile.create(filePath)
        val encodedFile = json.encodeToJsonElement(file)
        val decodedFile = json.decodeFromJsonElement<SystemFile>(encodedFile)
        assertEquals(file.name, decodedFile.name)

        val directory = Directory.create(Path.of(testDirectory), listOf(file))
        val encodedDirectory = json.encodeToJsonElement(directory)
        val decodedDirectory = json.decodeFromJsonElement<Directory>(encodedDirectory)
        assertEquals(directory.name, decodedDirectory.name)
    }
}
