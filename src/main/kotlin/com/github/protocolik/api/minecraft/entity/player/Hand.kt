package com.github.protocolik.api.minecraft.entity.player

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class Hand(
        val id: Int
) {
    MAIN_HAND(0),
    OFF_HAND(1);

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): Hand =
                byId[id.toInt()] ?: error("Unknown Hand with id: $id")
    }
}