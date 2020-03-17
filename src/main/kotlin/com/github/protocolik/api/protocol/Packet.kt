package com.github.protocolik.api.protocol

import com.github.protocolik.api.data.UserConnection
import io.netty.buffer.ByteBuf
import java.util.*

abstract class Packet(
        val packetType: PacketType
) {
    val uniqueId = UUID.randomUUID()
    var isCancelled: Boolean = false

    open fun read(byteBuf: ByteBuf, protocolVersion: ProtocolVersion, userConnection: UserConnection) =
            read(byteBuf, protocolVersion)

    abstract fun read(byteBuf: ByteBuf, protocolVersion: ProtocolVersion)

    open fun write(byteBuf: ByteBuf, protocolVersion: ProtocolVersion, userConnection: UserConnection) =
            write(byteBuf, protocolVersion)

    abstract fun write(byteBuf: ByteBuf, protocolVersion: ProtocolVersion)

    open fun remap(
            protocolVersion: ProtocolVersion,
            userConnection: UserConnection
    ): List<Packet> {
        return listOf(this)
    }

    open val packetName: String
        get() = javaClass.simpleName
}