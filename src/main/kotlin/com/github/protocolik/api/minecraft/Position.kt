@file:JvmName("Positions")

package com.github.protocolik.api.minecraft

import com.github.protocolik.api.protocol.ProtocolVersion
import com.github.protocolik.api.serialize.minecraft.Position1Dot14Serializer
import com.github.protocolik.api.serialize.minecraft.PositionSerializer
import io.netty.buffer.ByteBuf

data class Position
constructor(
        var x: Int = 0,
        var y: Int = 0,
        var z: Int = 0
)

fun ByteBuf.readPosition(protocolVersion: ProtocolVersion): Position =
        when {
            protocolVersion >= ProtocolVersion.RELEASE_1_14 -> Position1Dot14Serializer.read(this)
            protocolVersion >= ProtocolVersion.RELEASE_1_7 -> PositionSerializer.read(this)
            else -> error("Position not supported for $protocolVersion")
        }

fun ByteBuf.writePosition(position: Position, protocolVersion: ProtocolVersion) {
    when {
        protocolVersion >= ProtocolVersion.RELEASE_1_14 -> Position1Dot14Serializer.write(this, position)
        protocolVersion >= ProtocolVersion.RELEASE_1_7 -> PositionSerializer.write(this, position)
        else -> error("Position not supported for $protocolVersion")
    }
}