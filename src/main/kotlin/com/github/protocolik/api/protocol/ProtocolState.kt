package com.github.protocolik.api.protocol

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

enum class ProtocolState(
        val id: Int
) {
    HANDSHAKING(0),
    STATUS(1),
    LOGIN(2),
    PLAY(3);

    companion object {
        val values = values()
        private val byId = values.map { it.id to it }.toMap(Int2ObjectOpenHashMap())

        @JvmStatic
        operator fun get(id: Number): ProtocolState =
                byId[id.toInt()] ?: error("Unknown Protocol State: $id")
    }
}