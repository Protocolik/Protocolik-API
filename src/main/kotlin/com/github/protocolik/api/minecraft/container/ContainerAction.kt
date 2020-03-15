package com.github.protocolik.api.minecraft.container

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class ContainerAction(
        val id: Int
) {
    CLICK_ITEM(0),
    SHIFT_CLICK_ITEM(1),
    MOVE_TO_HOTBAR_SLOT(2),
    CREATIVE_GRAB_MAX_STACK(3),
    DROP_ITEM(4),
    SPREAD_ITEM(5),
    FILL_STACK(6);

    companion object {
        val value = values()
        private val byId = value.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): ContainerAction =
                byId[id.toInt()] ?: error("Unknown Container Action with id: $id")
    }
}