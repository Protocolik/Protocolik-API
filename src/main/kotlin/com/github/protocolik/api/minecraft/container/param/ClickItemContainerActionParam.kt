package com.github.protocolik.api.minecraft.container.param

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class ClickItemContainerActionParam(
        val id: Int
) : ContainerActionParam {
    LEFT_CLICK(0),
    RIGHT_CLICK(1);

    override val value: Int = id

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): ClickItemContainerActionParam =
                byId[id.toInt()] ?: error("Unknown Click Item Window Action Param with id: $id")
    }
}