package com.github.protocolik.api.minecraft.world.chunk

import com.github.protocolik.nbt.NBT

class Chunk1dot7(
        override val x: Int,
        override val z: Int,
        val unloadPacket: Boolean = true,
        override val isFull: Boolean = true,
        override val bitMask: Int = 0,
        override val sections: Array<ChunkSection?> = Array(16) { null },
        override var biomeData: IntArray? = null,
        override var heightMap: NBT = NBT().apply {
            set("MOTION_BLOCKING", LongArray(36))
        },
        override val blockEntities: List<NBT> = emptyList()
) : BaseChunk(x, z, isFull, bitMask, sections, biomeData, heightMap, blockEntities) {
    val hasBiomeData
        get() = biomeData != null && isFull
}