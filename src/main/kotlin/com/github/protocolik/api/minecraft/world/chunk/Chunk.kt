package com.github.protocolik.api.minecraft.world.chunk

import com.github.protocolik.api.minecraft.world.Dimension
import com.github.protocolik.api.protocol.ProtocolVersion
import com.github.protocolik.api.serialize.minecraft.*
import com.github.protocolik.nbt.NBT
import io.netty.buffer.ByteBuf

interface Chunk {
    val x: Int
    val z: Int
    val isFull: Boolean
    val bitMask: Int
    val sections: Array<ChunkSection?>
    var biomeData: IntArray?
    var heightMap: NBT
    val blockEntities: List<NBT>
}

fun ByteBuf.writeChunk(
        chunk: Chunk,
        dimension: Dimension = Dimension.OVERWORLD,
        protocolVersion: ProtocolVersion
) =
        when {
            protocolVersion >= ProtocolVersion.RELEASE_1_15 -> Chunk1Dot15Serializer.write(this, chunk)
            protocolVersion >= ProtocolVersion.RELEASE_1_14 -> Chunk1Dot14Serializer.write(this, chunk)
            protocolVersion >= ProtocolVersion.RELEASE_1_13 -> Chunk1Dot13Serializer(dimension).write(this, chunk)
            protocolVersion >= ProtocolVersion.RELEASE_1_9_4 -> Chunk1Dot9Dot4Serializer(dimension).write(this, chunk)
            protocolVersion >= ProtocolVersion.RELEASE_1_9 -> Chunk1Dot9Serializer(dimension).write(this, chunk)
            else -> error("ChunkSection not supported for $protocolVersion")
        }

fun ByteBuf.readChunk(
        dimension: Dimension = Dimension.OVERWORLD,
        protocolVersion: ProtocolVersion
): Chunk =
        when {
            protocolVersion >= ProtocolVersion.RELEASE_1_15 -> Chunk1Dot15Serializer.read(this)
            protocolVersion >= ProtocolVersion.RELEASE_1_14 -> Chunk1Dot14Serializer.read(this)
            protocolVersion >= ProtocolVersion.RELEASE_1_13 -> Chunk1Dot13Serializer(dimension).read(this)
            protocolVersion >= ProtocolVersion.RELEASE_1_9_4 -> Chunk1Dot9Dot4Serializer(dimension).read(this)
            protocolVersion >= ProtocolVersion.RELEASE_1_9 -> Chunk1Dot9Serializer(dimension).read(this)
            else -> error("ChunkSection not supported for $protocolVersion")
        }