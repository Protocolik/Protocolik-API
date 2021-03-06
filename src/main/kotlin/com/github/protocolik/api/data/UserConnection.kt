package com.github.protocolik.api.data

import com.github.protocolik.api.handler.PacketHandler
import com.github.protocolik.api.handler.PacketHandlerManager
import com.github.protocolik.api.minecraft.world.Dimension
import com.github.protocolik.api.protocol.Packet
import com.github.protocolik.api.protocol.ProtocolState
import com.github.protocolik.api.protocol.ProtocolVersion
import io.netty.channel.Channel
import io.netty.util.AttributeKey

data class UserConnection(
        val channel: Channel,
        var protocolVersion: ProtocolVersion = ProtocolVersion.CURRENT,
        var protocolState: ProtocolState = ProtocolState.HANDSHAKING,
        var username: String? = null
) : PacketHandlerManager {
    override val handlers = HashMap<Class<out Packet>, MutableList<PacketHandler<out Packet>>>()
    var dimension: Dimension = Dimension.OVERWORLD

    fun sendPacket(packet: Packet) {
        channel.writeAndFlush(packet)
//        channel.eventLoop().execute { channel.write(packet, channel.voidPromise()) }
    }

    fun receivePacket(packet: Packet?) {
        channel.eventLoop().execute { channel.pipeline().fireChannelRead(packet) }
    }

    companion object {
        val CHANNEL_ATTRIBUTE = AttributeKey.newInstance<UserConnection>("protocolik-user")

        fun getByChannel(channel: Channel): UserConnection {
            var connection = channel.attr(CHANNEL_ATTRIBUTE).get()
            return if (connection != null) {
                connection
            } else {
                connection = UserConnection(channel)
                channel.attr(CHANNEL_ATTRIBUTE).set(connection)
                connection
            }
        }
    }
}