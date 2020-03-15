package com.github.protocolik.api.minecraft.world.chunk

class NibbleArray(
        val data: ByteArray
) {
    constructor(length: Int) : this(ByteArray(length / 2))
}