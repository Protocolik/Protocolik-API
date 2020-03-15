package com.github.protocolik.api.minecraft.container

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class ContainerType(
        val id: Int
) {
    GENERIC_9X1(0),
    GENERIC_9X2(1),
    GENERIC_9X3(2),
    GENERIC_9X4(3),
    GENERIC_9X5(4),
    GENERIC_9X6(5),
    GENERIC_3X3(6),
    ANVIL(7),
    BEACON(8),
    BLAST_FURNACE(9),
    BREWING_STAND(10),
    CRAFTING(11),
    ENCHANTMENT(12),
    FURNACE(13),
    GRINDSTONE(14),
    HOPPER(15),
    LECTERN(16),
    LOOM(17),
    MERCHANT(18),
    SHULKER_BOX(19),
    SMOKER(20),
    CARTOGRAPHY(21),
    STONECUTTER(22);

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): ContainerType =
                byId[id.toInt()] ?: error("Unknown Container Type with id: $id")
    }
}