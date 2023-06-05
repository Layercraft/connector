package io.layercraft.connector.handler.handshake

import io.layercraft.connector.handler.PacketHandler
import io.layercraft.connector.utils.Connection
import io.layercraft.packetlib.packets.PacketState
import io.layercraft.packetlib.packets.v1_19_3.handshaking.serverbound.SetProtocolPacket

object SetProtocolPacketHandler: PacketHandler<SetProtocolPacket> {
    override fun handle(packet: SetProtocolPacket, connection: Connection) {
        connection.packetState = when (packet.nextState) {
            2 -> PacketState.LOGIN
            1 -> PacketState.STATUS
            else -> error("Invalid packet state")
        }

        connection.host = packet.serverHost
    }
}
