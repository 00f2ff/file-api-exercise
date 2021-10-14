package com

import com.data.Directory
import com.data.SystemFile
import com.data.WeaveData
import io.ktor.request.*
import kotlinx.serialization.Serializable
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

enum class Error(val value: String) {
    NOT_FOUND("file or directory not found"),
}

@Serializable
sealed class Result

@Serializable
data class Success(val data: WeaveData) : Result()

@Serializable
data class Failure(val error: Error) : Result()

/**
 * Container for API utilities
 */
object FileService {
    /**
     * Since the server accepts a form of wildcard input for file/directory path, we want to make sure
     * that input can be joined with the base path.
     */
    private fun sanitizeRequestPath(request: ApplicationRequest): String {
        return if (request.path().take(1) === "/") {
            request.path()
        } else {
            "/${request.path()}"
        }
    }

    /**
     * Given a base path supplied by the environment and another path supplied by the request, encapsulates
     * file data either as an individual file with content, or as a directory listing.
     */
    fun queryFileSystem(basePath: Path, request: ApplicationRequest): Result {
        val resolvedPath: Path = Path.of(basePath.toUri().path + sanitizeRequestPath(request))
        val exists = Files.exists(resolvedPath)
        if (!exists) {
            return Failure(Error.NOT_FOUND)
        }
        val fileData = if (Files.isDirectory(resolvedPath)) {
            val files = Files.list(resolvedPath).map {
                if (Files.isDirectory(it.toAbsolutePath())) {
                    Directory.create(it)
                } else {
                    SystemFile.create(it)
                }
            }.toList()
            Directory.create(resolvedPath, files)
        } else {
            SystemFile.create(resolvedPath, true)
        }

        return Success(fileData)
    }
}