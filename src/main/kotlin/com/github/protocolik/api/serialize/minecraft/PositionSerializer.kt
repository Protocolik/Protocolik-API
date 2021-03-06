package com.github.protocolik.api.serialize.minecraft

import com.github.protocolik.api.minecraft.Position
import com.github.protocolik.api.serialize.Serializer
import io.netty.buffer.ByteBuf

object PositionSerializer : Serializer<Position> {
    private const val POSITION_X_SIZE = 38
    private const val POSITION_Y_SIZE = 26
    private const val POSITION_Z_SIZE = 38
    private const val POSITION_Y_SHIFT = 0xFFF
    private const val POSITION_WRITE_SHIFT = 0x3FFFFFF

    override fun read(byteBuf: ByteBuf): Position {
        val value = byteBuf.readLong()
        val x = (value shr POSITION_X_SIZE).toInt()
        val y = (value shr POSITION_Y_SIZE and POSITION_Y_SHIFT.toLong()).toInt()
        val z = (value shl POSITION_Z_SIZE shr POSITION_Z_SIZE).toInt()
        return Position(x, y, z)
    }

    override fun write(byteBuf: ByteBuf, value: Position) {
        val x = (value.x and POSITION_WRITE_SHIFT).toLong()
        val y = (value.y and POSITION_Y_SHIFT).toLong()
        val z = (value.z and POSITION_WRITE_SHIFT).toLong()
        byteBuf.writeLong(x shl POSITION_X_SIZE or y shl POSITION_Y_SIZE or z)
    }
}