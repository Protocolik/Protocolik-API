package com.github.protocolik.api.protocol

import com.github.protocolik.api.data.VersionMapping
import com.google.gson.JsonObject

class Protocol
private constructor(
        val state: ProtocolState,
        val direction: ProtocolDirection
) : VersionMapping<PacketType>(PacketType::class.java) {
    init {
        clear()
    }

    companion object {
        val HANDSHAKING_SERVERBOUND = Protocol(ProtocolState.HANDSHAKING, ProtocolDirection.SERVERBOUND)
        val STATUS_CLIENTBOUND = Protocol(ProtocolState.STATUS, ProtocolDirection.CLIENTBOUND)
        val STATUS_SERVERBOUND = Protocol(ProtocolState.STATUS, ProtocolDirection.SERVERBOUND)
        val LOGIN_CLIENTBOUND = Protocol(ProtocolState.LOGIN, ProtocolDirection.CLIENTBOUND)
        val LOGIN_SERVERBOUND = Protocol(ProtocolState.LOGIN, ProtocolDirection.SERVERBOUND)
        val PLAY_CLIENTBOUND = Protocol(ProtocolState.PLAY, ProtocolDirection.CLIENTBOUND)
        val PLAY_SERVERBOUND = Protocol(ProtocolState.PLAY, ProtocolDirection.SERVERBOUND)

        fun load(jsonObject: JsonObject) {
            val packetTypes = PacketType.values().associateBy { it.name }
            val protocolVersions = ProtocolVersion.values().associateBy { it.displayName }
            for ((packetTypeName, mappingsJson) in jsonObject.entrySet()) {
                val packetType = packetTypes[packetTypeName.toUpperCase()]
                if (packetType != null) {
                    val mappings = mappingsJson.asJsonObject.entrySet().map { (versionName, hexId) ->
                        val protocolVersion = protocolVersions[versionName]
                        if (protocolVersion != null) {
                            val id = Integer.valueOf(hexId.asString.substring(2), 16)
                            protocolVersion to id
                        } else {
                            null
                        }
                    }.filterNotNull()
                    register(packetType, mappings)
                } else {
                    println("Unknown packet type: ${packetTypeName.toUpperCase()}")
                }
            }
        }

        fun register(packetType: PacketType, mappings: List<Pair<ProtocolVersion, Int>>) {
            if (mappings.isNotEmpty()) {
                val protocol = get(packetType.protocolState, packetType.protocolDirection)
                try {
                    protocol.register(packetType, *mappings.toTypedArray())
                } catch (e: Exception) {
                    println("packetType=$packetType")
                    println("mappings=")
                    mappings.forEach {
                        println(it)
                    }

                    throw e
                }
            }
        }

        operator fun get(
                protocolState: ProtocolState,
                protocolDirection: ProtocolDirection
        ): Protocol = when (protocolDirection) {
            ProtocolDirection.CLIENTBOUND -> {
                when (protocolState) {
                    ProtocolState.HANDSHAKING -> error("Handshaking clientbound? Something going wrong?")
                    ProtocolState.STATUS -> STATUS_CLIENTBOUND
                    ProtocolState.LOGIN -> LOGIN_CLIENTBOUND
                    ProtocolState.PLAY -> PLAY_CLIENTBOUND
                }
            }
            ProtocolDirection.SERVERBOUND -> {
                when (protocolState) {
                    ProtocolState.HANDSHAKING -> HANDSHAKING_SERVERBOUND
                    ProtocolState.STATUS -> STATUS_SERVERBOUND
                    ProtocolState.LOGIN -> LOGIN_SERVERBOUND
                    ProtocolState.PLAY -> PLAY_SERVERBOUND
                }
            }
        }
    }
}