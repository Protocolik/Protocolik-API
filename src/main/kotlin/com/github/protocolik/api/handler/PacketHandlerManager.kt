package com.github.protocolik.api.handler

import com.github.protocolik.api.data.UserConnection
import com.github.protocolik.api.protocol.Packet
import java.util.*

@Suppress("UNCHECKED_CAST")
interface PacketHandlerManager {
    val handlers: MutableMap<Class<out Packet>, MutableList<PacketHandler<out Packet>>>

    fun <T : Packet> registerHandler(clazz: Class<T>, packetHandler: PacketHandler<T>) {
        handlers.getOrPut(clazz) { LinkedList() }.add(packetHandler)
    }

    fun <T : Packet> unregisterHandler(packetHandler: PacketHandler<T>) {
        for ((clazz, handlersList) in handlers) {
            handlersList.remove(packetHandler)
        }
    }

    fun <T : Packet> getHandlers(clazz: Class<T>): List<PacketHandler<T>> =
            handlers.getOrPut(clazz) { LinkedList() } as List<PacketHandler<T>>

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

inline fun <reified T : Packet> PacketHandlerManager.registerHandler(packetHandler: PacketHandler<T>) =
        registerHandler(T::class.java, packetHandler)

inline fun <reified T : Packet> PacketHandlerManager.registerHandler(
        crossinline block: (T, UserConnection) -> Unit
) = registerHandler(object : PacketHandler<T> {
    override fun handle(packet: T, user: UserConnection) {
        block(packet, user)
    }
})