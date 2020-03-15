package com.github.protocolik.api.minecraft.world.chunk

import com.github.protocolik.api.protocol.ProtocolVersion
import com.github.protocolik.api.serialize.minecraft.ChunkSection1Dot13Serializer
import com.github.protocolik.api.serialize.minecraft.ChunkSection1Dot7Serializer
import com.github.protocolik.api.serialize.minecraft.ChunkSection1Dot9Serializer
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList

class ChunkSection {
    val palette = Palette()
    private val blocks = IntArray(SIZE)
    private var blockLight =
            NibbleArray(SIZE)
    private var skyLight: NibbleArray? = null
    var nonAirBlocksCount = 0

    fun setBlock(x: Int, y: Int, z: Int, type: Int, data: Int) {
        set(index(x, y, z), type shl 4 or (data and 0xf))
    }

    fun getBlockId(x: Int, y: Int, z: Int): Int = get(x, y, z) shr 4

    fun getBlockData(x: Int, y: Int, z: Int): Int = get(x, y, z) and 0xf

    operator fun set(x: Int, y: Int, z: Int, id: Int) = set(index(x, y, z), id)

    operator fun set(index: Int, id: Int) {
        var idx = palette.inverse[id]
        if (idx == -1) {
            idx = palette.size
            palette.add(id)
            palette.inverse[id] = index
        }
        setPaletteIndex(idx, index)
    }

    operator fun get(x: Int, y: Int, z: Int): Int = get(index(x, y, z))

    operator fun get(index: Int): Int {
        val idx = getPaletteIndex(index)
        return palette[idx]
    }

    fun getPaletteIndex(index: Int): Int = blocks[index]

    fun setPaletteIndex(index: Int, value: Int) {
        blocks[index] = value
    }

    fun readBlockLight(byteBuf: ByteBuf): ByteBuf = byteBuf.readBytes(blockLight.data)

    fun readSkyLight(byteBuf: ByteBuf): ByteBuf {
        val skyLight = skyLight ?: NibbleArray(
                SIZE
        )
        this.skyLight = skyLight
        return byteBuf.readBytes(skyLight.data)
    }

    fun writeBlockLight(byteBuf: ByteBuf): ByteBuf = byteBuf.writeBytes(blockLight.data)

    fun writeSkyLight(byteBuf: ByteBuf) {
        if (skyLight != null) {
            byteBuf.writeBytes(skyLight?.data)
        }
    }

    companion object {
        const val SIZE = 16 * 16 * 16 // width * depth * height
        const val LIGHT_LENGTH = SIZE / 2 // nibble bit count

        fun index(x: Int, y: Int, z: Int) = y shl 8 or (z shl 4) or x
    }

    class Palette {
        private var palette = IntArrayList()
        var inverse = Int2IntOpenHashMap()
        val size get() = palette.size

        operator fun get(index: Int): Int {
            return palette.getInt(index)
        }

        operator fun set(index: Int, id: Int) {
            val oldId = palette.set(index, id)
            if (oldId == id) {
                return
            }
            inverse[id] = index
            if (inverse[oldId] == index) {
                inverse.remove(oldId)
                for (i in palette.indices) {
                    if (palette.getInt(i) == oldId) {
                        inverse[oldId] = i
                        break
                    }
                }
            }
        }

        fun add(id: Int) {
            inverse[id] = palette.size
            palette.add(id)
        }

        fun replacePaletteEntry(oldId: Int, newId: Int) {
            val index: Int = inverse.remove(oldId)
            inverse[newId] = index
            for (i in palette.indices) {
                if (palette.getInt(i) == oldId) palette[i] = newId
            }
        }

        fun clear() {
            palette.clear()
            inverse.clear()
        }
    }
}

fun ByteBuf.writeChunkSection(chunkSection: ChunkSection, protocolVersion: ProtocolVersion) =
        when {
            protocolVersion >= ProtocolVersion.RELEASE_1_13 -> ChunkSection1Dot13Serializer.write(this, chunkSection)
            protocolVersion >= ProtocolVersion.RELEASE_1_9 -> ChunkSection1Dot9Serializer.write(this, chunkSection)
            protocolVersion >= ProtocolVersion.RELEASE_1_7 -> ChunkSection1Dot7Serializer.write(this, chunkSection)
            else -> error("ChunkSection not supported for $protocolVersion")
        }

fun ByteBuf.readChunkSection(protocolVersion: ProtocolVersion): ChunkSection =
        when {
            protocolVersion >= ProtocolVersion.RELEASE_1_13 -> ChunkSection1Dot13Serializer.read(this)
            protocolVersion >= ProtocolVersion.RELEASE_1_9 -> ChunkSection1Dot9Serializer.read(this)
            protocolVersion >= ProtocolVersion.RELEASE_1_7 -> ChunkSection1Dot7Serializer.read(this)
            else -> error("ChunkSection not supported for $protocolVersion")
        }
