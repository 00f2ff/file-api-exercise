package com.config

import com.data.Directory
import com.data.SystemFile
import com.data.WeaveData
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Polymorphic serializer that allows JSON encoding and decoding for classes that inherit from
 * @see WeaveData
 */
object WeaveDataSerializer : JsonContentPolymorphicSerializer<WeaveData>(WeaveData::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out WeaveData> {
        return when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            "directory" -> Directory.serializer()
            "file" -> SystemFile.serializer()
            else -> throw Exception("Unknown WeaveData: key 'type' not found")
        }
    }
}
