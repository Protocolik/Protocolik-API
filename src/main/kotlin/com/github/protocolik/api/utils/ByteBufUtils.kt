@file:JvmName("ByteBufUtils")

package com.github.protocolik.api.utils

import io.netty.buffer.ByteBuf
import java.io.IOException
import java.util.*

fun ByteBuf.writeVarInt(int: Int) {
    var i = int
    while (i and 0x7F.inv() != 0) {
        writeByte(i and 0x7F or 0x80)
        i = i ushr 7
    }
    writeByte(i)
}

fun ByteBuf.readVarInt(): Int {
    var value = 0
    var size = 0
    var b: Int
    while (true) {
        b = readByte().toInt()
        if (b and 0x80 != 0x80) break
        value = value or (b and 0x7F shl size++ * 7)
        if (size > 5) {
            throw IOException("VarInt too long (length must be <= 5)")
        }
    }
    return value or (b and 0x7F shl size * 7)
}

fun ByteBuf.readString(): String {
    val length = readVarInt()
    val bytes = ByteArray(length)
    readBytes(bytes)
    return bytes.toString(Charsets.UTF_8)
}

fun ByteBuf.writeString(string: String) {
    val bytes = string.toByteArray(Charsets.UTF_8)
    if (bytes.size > Short.MAX_VALUE) {
        throw IOException("String too big (was ${bytes.size} bytes encoded, max ${Short.MAX_VALUE})")
    } else {
        writeVarInt(bytes.size)
        writeBytes(bytes)
    }
}

fun ByteBuf.readUTF(): String {
    val length = readUnsignedShort()
    return ByteArray(length) {
        readByte()
    }.toString(Charsets.UTF_8)
}

fun ByteBuf.writeUTF(string: String) {
    writeShort(string.length)
    writeBytes(string.toByteArray(Charsets.UTF_8))
}

fun ByteBuf.readByteArray(): ByteArray =
        ByteArray(readVarInt()) {
            readByte()
        }

fun ByteBuf.writeByteArray(byteArray: ByteArray) {
    writeVarInt(byteArray.size)
    writeBytes(byteArray)
}

fun ByteBuf.readUUID(): UUID = UUID(readLong(), readLong())

fun ByteBuf.writeUUID(uuid: UUID) {
    writeLong(uuid.leastSignificantBits)
    writeLong(uuid.mostSignificantBits)
}

fun Int.getBitValue(bitmask: Int): Boolean = this and bitmask == bitmask

fun Int.setBitValue(value: Boolean, bitmask: Int): Int =
        if (value) {
            this or bitmask
        } else {
            this and bitmask.inv()
        }

fun ByteBuf.readLongs(length: Int): LongArray = LongArray(length) {
    readLong()
}

fun ByteBuf.writeLongs(longArray: LongArray) {
    longArray.forEach {
        writeLong(it)
    }
}