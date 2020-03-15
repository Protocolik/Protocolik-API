package com.github.protocolik.api.serialize.minecraft

import com.github.protocolik.api.minecraft.world.chunk.ChunkSection
import com.github.protocolik.api.serialize.Serializer
import com.github.protocolik.api.utils.readVarInt
import com.github.protocolik.api.utils.writeVarInt
import io.netty.buffer.ByteBuf
import kotlin.math.ceil

object ChunkSection1Dot13Serializer : Serializer<ChunkSection> {
    private const val GLOBAL_PALETTE = 14

    override fun read(byteBuf: ByteBuf): ChunkSection {
        val chunkSection = ChunkSection()

        // Reaad bits per block
        var bitsPerBlock: Int = byteBuf.readUnsignedByte().toInt()
        val originalBitsPerBlock = bitsPerBlock

        if (bitsPerBlock == 0 || bitsPerBlock > 8) {
            bitsPerBlock =
                    GLOBAL_PALETTE
        }

        val maxEntryValue = (1L shl bitsPerBlock) - 1

        val paletteLength = if (bitsPerBlock == GLOBAL_PALETTE) 0 else byteBuf.readVarInt()

        // Read palette
        chunkSection.palette.clear()
        for (i in 0 until paletteLength) {
            chunkSection.palette.add(byteBuf.readVarInt())
        }

        // Read blocks
        val blockData = LongArray(byteBuf.readVarInt())
        if (blockData.isNotEmpty()) {
            val expectedLength = ceil(ChunkSection.SIZE * bitsPerBlock / 64.0).toInt()
            check(blockData.size == expectedLength) { "Block data length (" + blockData.size + ") does not match expected length (" + expectedLength + ")! bitsPerBlock=" + bitsPerBlock + ", originalBitsPerBlock=" + originalBitsPerBlock }
            for (i in blockData.indices) {
                blockData[i] = byteBuf.readLong()
            }
            for (i in 0 until ChunkSection.SIZE) {
                val bitIndex = i * bitsPerBlock
                val startIndex = bitIndex / 64
                val endIndex = ((i + 1) * bitsPerBlock - 1) / 64
                val startBitSubIndex = bitIndex % 64
                val v = if (startIndex == endIndex) {
                    (blockData[startIndex] ushr startBitSubIndex and maxEntryValue).toInt()
                } else {
                    val endBitSubIndex = 64 - startBitSubIndex
                    ((blockData[startIndex] ushr startBitSubIndex or
                            (blockData[endIndex] shl endBitSubIndex)) and maxEntryValue).toInt()
                }
                if (bitsPerBlock == GLOBAL_PALETTE) {
                    chunkSection[i] = v
                } else {
                    chunkSection.setPaletteIndex(i, v)
                }
            }
        }

        return chunkSection
    }

    override fun write(byteBuf: ByteBuf, value: ChunkSection) {
        var bitsPerBlock = 4
        while (value.palette.size > 1 shl bitsPerBlock) {
            bitsPerBlock += 1
        }

        if (bitsPerBlock > 8) {
            bitsPerBlock =
                    GLOBAL_PALETTE
        }

        val maxEntryValue = (1L shl bitsPerBlock) - 1
        byteBuf.writeByte(bitsPerBlock)

        // Write pallet (or not)
        if (bitsPerBlock != GLOBAL_PALETTE) {
            byteBuf.writeVarInt(value.palette.size)
            for (i in 0 until value.palette.size) {
                byteBuf.writeVarInt(value.palette[i])
            }
        }

        val length = ceil(ChunkSection.SIZE * bitsPerBlock / 64.0).toInt()
        byteBuf.writeVarInt(length)
        val data = LongArray(length)
        for (index in 0 until ChunkSection.SIZE) {
            val v: Int = if (bitsPerBlock == GLOBAL_PALETTE) {
                value[index]
            } else {
                value.getPaletteIndex(index)
            }
            val bitIndex = index * bitsPerBlock
            val startIndex = bitIndex / 64
            val endIndex = ((index + 1) * bitsPerBlock - 1) / 64
            val startBitSubIndex = bitIndex % 64
            data[startIndex] =
                    data[startIndex] and (maxEntryValue shl startBitSubIndex).inv() or
                            ((v.toLong() and maxEntryValue) shl startBitSubIndex)
            if (startIndex != endIndex) {
                val endBitSubIndex = 64 - startBitSubIndex
                data[endIndex] =
                        data[endIndex] ushr endBitSubIndex shl endBitSubIndex or
                                ((v.toLong() and maxEntryValue) shr endBitSubIndex)
            }
        }
        for (l in data) {
            byteBuf.writeLong(l)
        }
    }
}

