package com.github.protocolik.api.serialize

import io.netty.buffer.ByteBuf

interface ByteBufReader<T> {
    fun read(byteBuf: ByteBuf): T
}

interface ByteBufWriter<T> {
    fun write(byteBuf: ByteBuf, value: T)
}

interface Serializer<T> : ByteBufReader<T>, ByteBufWriter<T>

interface PartialSerializer<T, X> : Serializer<T> {
    val param: X

    fun read(byteBuf: ByteBuf, param: X): T

    fun write(byteBuf: ByteBuf, param: X, value: T)

    override fun read(byteBuf: ByteBuf): T = read(byteBuf, param)

    override fun write(byteBuf: ByteBuf, value: T) = write(byteBuf, param, value)
}

fun <T> ByteBuf.write(serializer: Serializer<T>, value: T) = serializer.write(this, value)

fun <T> ByteBuf.read(serializer: Serializer<T>): T = serializer.read(this)