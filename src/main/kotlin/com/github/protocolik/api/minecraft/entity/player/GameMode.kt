package com.github.protocolik.api.minecraft.entity.player

import com.github.protocolik.api.minecraft.world.notify.ClientNotificationValue
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class GameMode(
        val id: Int
) : ClientNotificationValue {
    CREATIVE(1),
    SURVIVAL(0),
    ADVENTURE(2),
    SPECTATOR(3);

    override val value: Float = id.toFloat()

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): GameMode =
                byId[id.toInt()] ?: error("Unknown GameMode with id: $id")
    }
}