package com.github.protocolik.api.protocol

import com.github.protocolik.api.data.VersionMapping

class Protocol
private constructor(
        val state: ProtocolState,
        val direction: ProtocolDirection
) : VersionMapping<PacketType>(PacketType::class.java) {
    init {
        clear()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Packet> createPacket(protocolVersion: ProtocolVersion, id: Int): T? {
//        val packetType = get(protocolVersion)
//        return packetType[id]?.createPacket?.invoke() as T?
        TODO()
    }

    companion object {
        val HANDSHAKING_SERVERBOUND = Protocol(ProtocolState.HANDSHAKING, ProtocolDirection.SERVERBOUND)
        val STATUS_CLIENTBOUND = Protocol(ProtocolState.STATUS, ProtocolDirection.CLIENTBOUND)
        val STATUS_SERVERBOUND = Protocol(ProtocolState.STATUS, ProtocolDirection.SERVERBOUND)
        val LOGIN_CLIENTBOUND = Protocol(ProtocolState.LOGIN, ProtocolDirection.CLIENTBOUND)
        val LOGIN_SERVERBOUND = Protocol(ProtocolState.LOGIN, ProtocolDirection.SERVERBOUND)
        val PLAY_CLIENTBOUND = Protocol(ProtocolState.PLAY, ProtocolDirection.CLIENTBOUND)
        val PLAY_SERVERBOUND = Protocol(ProtocolState.PLAY, ProtocolDirection.SERVERBOUND)

        fun load() {
            for (value in PacketType.values()) {
                register(value)
            }
        }

        fun register(packetType: PacketType) {
//            if (packetType.idMappings.isNotEmpty()) {
//                val protocol = get(packetType.protocolState, packetType.protocolDirection)
//                protocol.register(packetType, *packetType.idMappings)
//            }
            TODO()
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