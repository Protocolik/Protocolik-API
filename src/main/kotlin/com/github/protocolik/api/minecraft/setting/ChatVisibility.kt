package com.github.protocolik.api.minecraft.setting

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class ChatVisibility(
        val id: Int
) {
    ENABLED(0),
    COMMANDS_ONLY(1),
    HIDDEN(2);

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): ChatVisibility =
                byId[id.toInt()] ?: error("Unknown Chat mode with id: $id")
    }
}