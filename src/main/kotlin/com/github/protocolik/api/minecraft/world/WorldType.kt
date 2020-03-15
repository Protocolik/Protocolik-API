package com.github.protocolik.api.minecraft.world

enum class WorldType(
        val namespace: String
) {
    DEFAULT("default"),
    FLAT("flat"),
    DEFAULT_1_1("default_1_1"),
    LARGE_BIOMES("largeBiomes"),
    AMPLIFIED("amplified"),
    CUSTOMIZED("customized"),
    BUFFET("buffet");

    companion object {
        val values = values()
        private val byNamespace = values.map { it.namespace to it }.toMap()

        @JvmStatic
        operator fun get(namespace: String): WorldType =
                byNamespace[namespace] ?: error("Unknown world type: '$namespace'")
    }
}
