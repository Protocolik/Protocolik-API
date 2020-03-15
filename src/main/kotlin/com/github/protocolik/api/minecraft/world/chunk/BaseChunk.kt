package com.github.protocolik.api.minecraft.world.chunk

import com.github.protocolik.nbt.NBT


open class BaseChunk(
        override val x: Int,
        override val z: Int,
        override val isFull: Boolean = true,
        override val bitMask: Int = 0,
        override val sections: Array<ChunkSection?> = Array(16) { null },
        override var biomeData: IntArray? = null,
        override var heightMap: NBT = NBT().apply {
            set("MOTION_BLOCKING", LongArray(36))
        },
        override val blockEntities: List<NBT> = emptyList()
) : Chunk