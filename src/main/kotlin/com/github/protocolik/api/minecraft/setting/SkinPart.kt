package com.github.protocolik.api.minecraft.setting

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class SkinPart(
        val id: Int
) {
    CAPE(0x01),
    JACKET(0x02),
    LEFT_SLEEVE(0x04),
    RIGHT_SLEEVE(0x08),
    LEFT_PANTS_LEG(0x10),
    RIGHT_PANTS_LEG(0x20),
    HAT(0x40);

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): SkinPart =
                byId[id.toInt()] ?: error("Unknown SkinPart with id: $id")
    }
}