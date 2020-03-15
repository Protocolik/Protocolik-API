package com.github.protocolik.api.minecraft.container.param

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class DropItemContainerActionParam(
        val id: Int
) : ContainerActionParam {
    DROP_FROM_SELECTED(0),
    DROP_SELECTED_STACK(1),
    LEFT_CLICK_OUTSIDE_NOT_HOLDING(2),
    RIGHT_CLICK_OUTSIDE_NOT_HOLDING(3);

    override val value: Int = id

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): DropItemContainerActionParam =
                byId[id.toInt()] ?: error("Unknown Drop Item Window Action Param with id: $id")
    }
}