package com.data

import kotlinx.serialization.Serializable
import java.nio.file.Path

@Serializable
data class Directory(
    val name: String,
    val files: List<WeaveData>? = null,
    override val type: String = "directory"
) : WeaveData() {
    companion object {
        fun create(path: Path, files: List<WeaveData>? = null): Directory =
            Directory(name = "${path.fileName}/", files = files)
    }
}
