package io.layercraft.connector.handler.status

import io.layercraft.connector.handler.PacketHandler
import io.layercraft.connector.utils.Connection
import io.layercraft.connector.utils.sendMinecraftPacket

object PingPacketHandler: PacketHandler<io.layercraft.packetlib.packets.v1_19_3.status.serverbound.PingPacket> {
    override fun handle(packet: io.layercraft.packetlib.packets.v1_19_3.status.serverbound.PingPacket, connection: Connection) {
        val response = io.layercraft.packetlib.packets.v1_19_3.status.clientbound.PingPacket(packet.time)
        connection.sendMinecraftPacket(response)
    }
}
