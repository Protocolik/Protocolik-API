package com.github.protocolik.api.minecraft

import com.github.protocolik.api.serialize.Serializer
import com.github.protocolik.api.utils.readVarInt
import com.github.protocolik.api.utils.writeVarInt
import com.github.protocolik.nbt.NBT
import com.github.protocolik.nbt.io.readNBT
import com.github.protocolik.nbt.io.writeNBT
import io.netty.buffer.ByteBuf

data class ItemStack
@JvmOverloads
constructor(
        val id: Int = -1,
        val amount: Int = 1,
        val nbt: NBT? = null
) {
    companion object : Serializer<ItemStack?> {
        override fun read(byteBuf: ByteBuf): ItemStack? {
            return if (byteBuf.readBoolean()) {
                return ItemStack(byteBuf.readVarInt(), byteBuf.readByte().toInt(), byteBuf.readNBT())
            } else {
                null
            }
        }

        override fun write(byteBuf: ByteBuf, value: ItemStack?) {
            byteBuf.writeBoolean(value != null)
            if (value != null) {
                byteBuf.writeVarInt(value.id)
                byteBuf.writeByte(value.amount)
                if (value.nbt != null) {
                    byteBuf.writeNBT(value.nbt)
                } else {
                    byteBuf.writeByte(0)
                }
            }
        }
    }
}

fun ByteBuf.readItemStack(): ItemStack? = ItemStack.read(this)
fun ByteBuf.writeItemStack(itemStack: ItemStack?) = ItemStack.write(this, itemStack)