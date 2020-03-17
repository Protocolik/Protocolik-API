package com.github.protocolik.api

import com.github.protocolik.api.handler.PacketHandler
import com.github.protocolik.api.handler.PacketHandlerManager
import com.github.protocolik.api.protocol.Packet
import java.io.File
import java.util.logging.Logger

object Protocolik : PacketHandlerManager {
    val log: Logger = Logger.getLogger("Protocolik")
    var dataFolder: File = File("")
    override val handlers = HashMap<Class<out Packet>, MutableList<PacketHandler<out Packet>>>()
}

val log get() = Protocolik.log
val dataFolder get() = Protocolik.dataFolder