package com.github.protocolik.api.serialize.minecraft

import com.github.protocolik.api.minecraft.world.Dimension
import com.github.protocolik.api.minecraft.world.chunk.*
import com.github.protocolik.api.protocol.ProtocolVersion
import com.github.protocolik.api.serialize.PartialSerializer
import com.github.protocolik.api.utils.readVarInt
import com.github.protocolik.api.utils.writeVarInt
import io.netty.buffer.ByteBuf
import java.util.*

class Chunk1Dot9Serializer(
        override val param: Dimension
) : PartialSerializer<Chunk, Dimension> {
    override fun read(byteBuf: ByteBuf, param: Dimension): Chunk {
        val chunkX = byteBuf.readInt()
        val chunkZ = byteBuf.readInt()

        val isFullChunk = byteBuf.readBoolean()
        val primaryBitmask = byteBuf.readVarInt()
        // Size (unused)
        byteBuf.readVarInt()

        val usedSections = BitSet(16)
        // Calculate section count from bitmask
        for (i in 0..15) {
            if (primaryBitmask and (1 shl i) != 0) {
                usedSections.set(i)
            }
        }

        // Read sections
        val sections = Array(16) {
            if (usedSections[it]) { // Section not set
                val section: ChunkSection = byteBuf.readChunkSection(ProtocolVersion.RELEASE_1_9)
                section.readBlockLight(byteBuf)
                if (param == Dimension.OVERWORLD) {
                    section.readSkyLight(byteBuf)
                }
                section
            } else {
                null
            }
        }

        // Read biome data
        val biomeData = if (isFullChunk) {
            IntArray(256) {
                byteBuf.readByte().toInt() and 0xff
            }
        } else {
            null
        }

        return BaseChunk(
                chunkX,
                chunkZ,
                isFullChunk,
                primaryBitmask,
                sections,
                biomeData
        )
    }

    override fun write(byteBuf: ByteBuf, param: Dimension, value: Chunk) {
        byteBuf.writeInt(value.x)
        byteBuf.writeInt(value.z)

        byteBuf.writeBoolean(value.isFull)
        byteBuf.writeVarInt(value.bitMask)

        val buf: ByteBuf = byteBuf.alloc().buffer()
        try {
            for (i in 0..15) {
                val section: ChunkSection = value.sections[i] ?: continue
                // Section not set
                buf.writeChunkSection(section, ProtocolVersion.RELEASE_1_9)
                section.writeBlockLight(buf)
                section.writeSkyLight(buf)
            }
            buf.readerIndex(0)
            byteBuf.writeVarInt(buf.readableBytes() + if (value.biomeData != null) 256 else 0)
            byteBuf.writeBytes(buf)
        } finally {
            buf.release() // release buffer
        }

        // Write biome data
        val biomeData = value.biomeData
        if (value.isFull && biomeData != null) {
            for (it in biomeData) {
                byteBuf.writeByte(it)
            }
        }
    }
}