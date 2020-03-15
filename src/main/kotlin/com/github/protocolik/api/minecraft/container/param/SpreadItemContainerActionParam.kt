package com.github.protocolik.api.minecraft.container.param

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class SpreadItemContainerActionParam(
        val id: Int
) : ContainerActionParam {
    LEFT_MOUSE_BEGIN_DRAG(0),
    LEFT_MOUSE_ADD_SLOT(1),
    LEFT_MOUSE_END_DRAG(2),
    RIGHT_MOUSE_BEGIN_DRAG(3),
    RIGHT_MOUSE_ADD_SLOT(4),
    RIGHT_MOUSE_END_DRAG(5),
    MIDDLE_MOUSE_BEGIN_DRAG(6),
    MIDDLE_MOUSE_ADD_SLOT(7),
    MIDDLE_MOUSE_END_DRAG(8);

    override val value: Int = id

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): SpreadItemContainerActionParam =
                byId[id.toInt()] ?: error("Unknown Spread Item Window Action Param with id: $id")
    }
}