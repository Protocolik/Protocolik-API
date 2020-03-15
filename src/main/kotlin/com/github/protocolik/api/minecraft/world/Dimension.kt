package com.github.protocolik.api.minecraft.world

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class Dimension(
        val id: Int
) {
    OVERWORLD(0),
    NETHER(-1),
    END(1);

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): Dimension =
                byId[id.toInt()] ?: error("Unknown dimension with id: $id")
    }
}

