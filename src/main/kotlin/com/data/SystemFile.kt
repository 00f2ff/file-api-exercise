package com.data

import kotlinx.serialization.Serializable
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.getOwner

@Serializable
data class SystemFile(
    val name: String,
    val owner: String,
    val size: Long,
    val permissions: String,
    val contents: String? = null
) : WeaveData() {
    companion object {
        fun create(path: Path, includeContents: Boolean = false): SystemFile {
            val filePath = path.toAbsolutePath()
            val contents = if (includeContents) Files.readString(path) else null
            return SystemFile(
                name = path.fileName.toString(),
                owner = path.getOwner().toString(),
                size = Files.size(filePath),
                permissions = Files.getPosixFilePermissions(filePath).toString(),
                contents = contents
            )
        }
    }
}