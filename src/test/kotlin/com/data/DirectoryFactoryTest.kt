package com.data

import com.FileSystemTest
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.test.assertEquals

class DirectoryFactoryTest : FileSystemTest() {

    @Test
    fun `a directory can be created using just a path`() {
        val directoryPath = Path.of("$testDirectory/newDir")
        val filePath = Path.of("${directoryPath.absolutePathString()}/hello")
        Files.createDirectory(directoryPath)
        Files.createFile(filePath)

        val file = SystemFile.create(filePath)
        val directory = Directory.create(directoryPath, listOf(file))
        assertEquals("newDir/", directory.name)
    }
}
