package com.github.protocolik.api.minecraft.container

import com.github.protocolik.api.minecraft.container.param.ContainerActionParam
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class CraftingBookDataType(
        val id: Int
) : ContainerActionParam {
    DISPLAYED_RECIPE(0),
    CRAFTING_BOOK_STATUS(1);

    override val value: Int = id

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): CraftingBookDataType =
                byId[id.toInt()] ?: error("Unknown Crafting Book Data Type with id: $id")
    }
}