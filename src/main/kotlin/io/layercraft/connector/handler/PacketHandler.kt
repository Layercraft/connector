package io.layercraft.connector.handler

import io.layercraft.connector.utils.Connection
import io.layercraft.packetlib.packets.Packet

interface PacketHandler<T: Packet> {

    fun handle(packet: T, connection: Connection)
}
