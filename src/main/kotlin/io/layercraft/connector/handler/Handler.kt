package io.layercraft.connector.handler

import io.layercraft.connector.handler.handshake.SetProtocolPacketHandler
import io.layercraft.connector.handler.login.LoginStartPacketHandler
import io.layercraft.connector.handler.status.PingPacketHandler
import io.layercraft.connector.handler.status.PingStartPacketHandler
import io.layercraft.connector.utils.Connection
import io.layercraft.packetlib.packets.Packet
import io.layercraft.packetlib.packets.v1_19_3.handshaking.serverbound.SetProtocolPacket
import io.layercraft.packetlib.packets.v1_19_3.login.serverbound.LoginStartPacket
import io.layercraft.packetlib.packets.v1_19_3.status.serverbound.PingStartPacket
import java.util.logging.Logger
import kotlin.reflect.KClass

object Handler {

    private val logger = Logger.getLogger(Handler::class.java.name)

    private val handlers: Map<KClass<*>, PacketHandler<out Packet>> = mapOf(
        // Handshake
        SetProtocolPacket::class to SetProtocolPacketHandler,
        // Status
        io.layercraft.packetlib.packets.v1_19_3.status.serverbound.PingPacket::class to PingPacketHandler,
        PingStartPacket::class to PingStartPacketHandler,
        // Login
        LoginStartPacket::class to LoginStartPacketHandler,

        // Play
    )

    private fun <T : Packet> getHandler(packet: T): PacketHandler<T>? {
        if (handlers.containsKey(packet::class)) {
            @Suppress("UNCHECKED_CAST")
            return handlers[packet::class] as PacketHandler<T>?
        }
        return null
    }

    fun handle(packet: Packet, connection: Connection) {
        logger.info("Handling packet $packet")

        getHandler(packet)?.handle(packet, connection)
    }
}
