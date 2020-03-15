package com.github.protocolik.api.minecraft.entity.player

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class InteractAction(
        val id: Int
) {
    INTERACT(0),
    ATTACK(1),
    INTERACT_AT(2);

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): InteractAction =
                byId[id.toInt()] ?: error("Unknown Interact Action with id: $id")
    }
}