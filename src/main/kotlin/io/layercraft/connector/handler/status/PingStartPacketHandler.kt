package io.layercraft.connector.handler.status

import io.layercraft.connector.CODEC
import io.layercraft.connector.SERVER_UUID
import io.layercraft.connector.VERSION
import io.layercraft.connector.handler.PacketHandler
import io.layercraft.connector.utils.Connection
import io.layercraft.connector.utils.sendMinecraftPacket
import io.layercraft.packetlib.packets.v1_19_3.status.clientbound.ServerInfoPacket
import io.layercraft.packetlib.packets.v1_19_3.status.serverbound.PingStartPacket

object PingStartPacketHandler: PacketHandler<PingStartPacket> {
    override fun handle(packet: PingStartPacket, connection: Connection) {
        val json = """
            {"version":{"name":"Layercraft-$VERSION","protocol":${CODEC.protocolVersion.protocolNumber}},"players":{"max":250,"online":10},"description":{"text":"Connector: $SERVER_UUID"},"previewsChat":false,"enforcesSecureChat":false}
        """.trimIndent().trim()

        val response = ServerInfoPacket(json)

        connection.socket.sendMinecraftPacket(response)
    }

}
