package com.github.protocolik.api.minecraft

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class Difficulty(
        val id: Int
) {
    PEACEFUL(0),
    EASY(1),
    NORMAL(2),
    HARD(3);

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): Difficulty =
                byId[id.toInt()] ?: error("Unknown Difficulty with id: $id")
    }
}