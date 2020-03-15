package com.github.protocolik.api.minecraft.world.notify

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

interface ClientNotificationValue {
    val value: Float

    data class BaseValue(override val value: Float = 0.0f) : ClientNotificationValue

    enum class DemoMessage(
            value: Int
    ) : ClientNotificationValue {
        WELCOME(0),
        MOVEMENT_CONTROLS(1),
        JUMP_CONTROL(2),
        INVENTORY_CONTROL(3);

        override val value: Float = value.toFloat()
        val id: Int = value

        companion object {
            val values = values()
            private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

            @JvmStatic
            operator fun get(id: Number): DemoMessage =
                    byId[id.toInt()] ?: error("Unknown Demo message with id: $id")
        }
    }

    enum class EnterCredits(
            value: Int
    ) : ClientNotificationValue {
        SEEN_BEFORE(0),
        FIRST_TIME(1);

        override val value: Float = value.toFloat()
        val id: Int = value

        companion object {
            val values = values()
            private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

            @JvmStatic
            operator fun get(id: Number): EnterCredits =
                    byId[id.toInt()] ?: error("Unknown Enter credits with id: $id")
        }
    }
}