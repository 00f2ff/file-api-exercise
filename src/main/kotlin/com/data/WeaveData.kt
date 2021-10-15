package com.data

import com.config.WeaveDataSerializer
import kotlinx.serialization.Serializable

/**
 * Common "trait" used for file / directory inheritance
 */
@Serializable(with = WeaveDataSerializer::class)
sealed class WeaveData {
    abstract val type: String
}
