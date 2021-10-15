package com

import kotlinx.serialization.json.Json
import org.junit.Before
import java.nio.file.Files
import java.nio.file.Path

open class FileSystemTest {

    protected val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
        encodeDefaults = true
        classDiscriminator = "#class"
    }

    private val workingDirectory = System.getProperty("user.dir")
    protected val testDirectory = "$workingDirectory/testDir"

    /**
     * Recursively delete the test directory, and then recreate an empty version
     */
    @Before
    fun setup() {
        val testDirectoryPath = Path.of(testDirectory)
        if (Files.exists(testDirectoryPath)) {
            Files.walk(testDirectoryPath).sorted(Comparator.reverseOrder()).forEach { path ->
                Files.delete(path)
            }
        }
        Files.createDirectory(testDirectoryPath)
    }
}
