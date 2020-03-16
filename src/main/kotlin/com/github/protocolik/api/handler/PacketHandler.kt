package com.github.protocolik.api.handler

import com.github.protocolik.api.data.UserConnection
import com.github.protocolik.api.protocol.Packet

@FunctionalInterface
interface PacketHandler<T : Packet> {
    fun handle(packet: T, user: UserConnection)
}