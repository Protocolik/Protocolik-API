package com.github.protocolik.api.serialize.minecraft

import com.github.protocolik.api.minecraft.world.chunk.*
import com.github.protocolik.api.protocol.ProtocolVersion
import com.github.protocolik.api.serialize.Serializer
import com.github.protocolik.api.utils.readVarInt
import com.github.protocolik.api.utils.writeVarInt
import com.github.protocolik.nbt.NBT
import com.github.protocolik.nbt.io.readNBT
import com.github.protocolik.nbt.io.writeNBT
import io.netty.buffer.ByteBuf
import java.util.*

object Chunk1Dot15Serializer : Serializer<Chunk> {
    override fun read(byteBuf: ByteBuf): Chunk {
        val chunkX = byteBuf.readInt()
        val chunkZ = byteBuf.readInt()

        val isFullChunk = byteBuf.readBoolean()
        val primaryBitmask = byteBuf.readVarInt()
        val heightMap = byteBuf.readNBT()

        val biomeData = if (isFullChunk) {
            IntArray(1024) {
                byteBuf.readInt()
            }
        } else {
            null
        }

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
                val nonAirBlocksCount = byteBuf.readShort().toInt()
                val section: ChunkSection = byteBuf.readChunkSection(ProtocolVersion.RELEASE_1_15)
                section.nonAirBlocksCount = nonAirBlocksCount
                section
            } else {
                null
            }
        }

        val nbtData: List<NBT> = List(byteBuf.readVarInt()) {
            byteBuf.readNBT()
        }

        return BaseChunk(
                chunkX,
                chunkZ,
                isFullChunk,
                primaryBitmask,
                sections,
                biomeData,
                heightMap,
                nbtData
        )
    }

    override fun write(byteBuf: ByteBuf, value: Chunk) {
        byteBuf.writeInt(value.x)
        byteBuf.writeInt(value.z)

        byteBuf.writeBoolean(value.isFull)
        byteBuf.writeVarInt(value.bitMask)
        byteBuf.writeNBT(value.heightMap)

        // Write biome data
        val biomeData = value.biomeData
        if (value.isFull && biomeData != null) {
            for (it in biomeData) {
                byteBuf.writeInt(it)
            }
        }

        val buf: ByteBuf = byteBuf.alloc().buffer()
        try {
            for (i in 0..15) {
                val section: ChunkSection = value.sections[i] ?: continue
                // Section not set
                buf.writeShort(section.nonAirBlocksCount)
                buf.writeChunkSection(section, ProtocolVersion.RELEASE_1_15)
            }
            buf.readerIndex(0)
            byteBuf.writeVarInt(buf.readableBytes())
            byteBuf.writeBytes(buf)
        } finally {
            buf.release() // release buffer
        }

        // Write Block Entities
        byteBuf.writeVarInt(value.blockEntities.size)
        for (blockEntity in value.blockEntities) {
            byteBuf.writeNBT(blockEntity)
        }
    }
}