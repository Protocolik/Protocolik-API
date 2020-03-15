package com.github.protocolik.api.serialize.minecraft

import com.github.protocolik.api.minecraft.world.chunk.ChunkSection
import com.github.protocolik.api.serialize.Serializer
import io.netty.buffer.ByteBuf

object ChunkSection1Dot7Serializer : Serializer<ChunkSection> {
    override fun read(byteBuf: ByteBuf): ChunkSection {
        val chunkSection = ChunkSection()

        for (i in 0 until ChunkSection.SIZE) {
            val blockId: Int = byteBuf.readShortLE().toInt()
            chunkSection[i] = blockId
        }

        return chunkSection
    }

    override fun write(byteBuf: ByteBuf, value: ChunkSection) {
        for (i in 0 until ChunkSection.SIZE) {
            val blockId = value[i]
            byteBuf.writeShortLE(blockId)
        }
    }
}