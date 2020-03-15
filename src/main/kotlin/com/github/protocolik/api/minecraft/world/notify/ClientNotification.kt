package com.github.protocolik.api.minecraft.world.notify

import com.github.protocolik.api.minecraft.entity.player.GameMode
import com.github.protocolik.api.minecraft.world.notify.ClientNotificationValue.*
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class ClientNotification(
        val id: Int,
        private val notificationValueCreator: (Float) -> ClientNotificationValue = { BaseValue(it) }
) {
    INVALID_BED(0),
    START_RAIN(1),
    STOP_RAIN(2),
    CHANGE_GAMEMODE(3, { GameMode[it] }),
    ENTER_CREDITS(4, { EnterCredits[it] }),
    DEMO_MESSAGE(5, { DemoMessage[it] }),
    ARROW_HIT_PLAYER(6),
    RAIN_STRENGTH(7),
    THUNDER_STRENGTH(8),
    AFFECTED_BY_ELDER_GUARDIAN(9);

    operator fun get(float: Float): ClientNotificationValue = notificationValueCreator(float)

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): ClientNotification =
                byId[id] ?: error("Unknown Client notification with id: $id")
    }
}