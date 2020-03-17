package com.github.protocolik.api.data

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
) {
    var dimension: Dimension = Dimension.OVERWORLD

    fun sendPacket(packet: Packet) {
        channel.writeAndFlush(packet)
//        channel.eventLoop().execute { channel.write(packet, channel.voidPromise()) }
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