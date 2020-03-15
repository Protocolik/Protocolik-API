package com.github.protocolik.api.minecraft.container.param

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class FillStackContainerActionParam(
        val id: Int
) : ContainerActionParam {
    FILL(0);

    override val value: Int = id

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): FillStackContainerActionParam =
                byId[id.toInt()] ?: error("Unknown Fill Stack Window Action Param with id: $id")
    }
}