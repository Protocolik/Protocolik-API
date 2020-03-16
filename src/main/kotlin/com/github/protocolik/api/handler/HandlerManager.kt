package com.github.protocolik.api.handler

import com.github.protocolik.api.data.UserConnection
import com.github.protocolik.api.protocol.Packet

object HandlerManager {
    private val handlers = HashMap<Class<out Packet>, MutableList<PacketHandler<out Packet>>>()

    inline fun <reified T : Packet> registerHandler(packetHandler: PacketHandler<T>) =
            registerHandler(T::class.java, packetHandler)

    inline fun <reified T : Packet> registerHandler(
            crossinline block: (T, UserConnection) -> Unit
    ) = registerHandler(object : PacketHandler<T> {
        override fun handle(packet: T, user: UserConnection) {
            block(packet, user)
        }
    })

    @JvmStatic
    fun <T : Packet> registerHandler(clazz: Class<T>, packetHandler: PacketHandler<T>) {
        handlers.getOrPut(clazz) { ArrayList() }.add(packetHandler)
    }

    @JvmStatic
    fun <T : Packet> callHandlers(packet: T, userConnection: UserConnection) {
        handlers[packet::class.java]?.forEach {
            @Suppress("UNCHECKED_CAST")
            it as PacketHandler<T>
            try {
                it.handle(packet, userConnection)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}