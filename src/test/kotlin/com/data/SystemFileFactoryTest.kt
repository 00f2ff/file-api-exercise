package com.data

import com.FileSystemTest
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SystemFileFactoryTest : FileSystemTest() {

    @Test
    fun `a file can be created using just a path`() {
        val filePath = Path.of("$testDirectory/.dotfile")
        Files.createFile(filePath)
        val message = "hi"
        Files.writeString(filePath, message)

        val file = SystemFile.create(filePath)
        assertEquals(".dotfile", file.name)
        assertNull(file.contents)

        val fileWithContent = SystemFile.create(path = filePath, includeContents = true)
        assertEquals(message, fileWithContent.contents)
    }
}
