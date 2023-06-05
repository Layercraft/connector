package io.layercraft.connector.handler.login

import io.layercraft.connector.SERVER_ID
import io.layercraft.connector.handler.PacketHandler
import io.layercraft.connector.utils.Connection
import io.layercraft.connector.utils.EncryptionUtils
import io.layercraft.connector.utils.sendMinecraftPacket
import io.layercraft.packetlib.packets.v1_19_3.login.clientbound.EncryptionBeginPacket
import io.layercraft.packetlib.packets.v1_19_3.login.serverbound.LoginStartPacket

object LoginStartPacketHandler: PacketHandler<LoginStartPacket> {
    override fun handle(packet: LoginStartPacket, connection: Connection) {
        connection.username = packet.username
        if (packet.hasPlayerUUID) connection.mcUUID = packet.playerUUID!!

        val response = EncryptionBeginPacket(
            SERVER_ID,
            EncryptionUtils.publicKey.encoded,
            connection.verifyToken,
        )

        connection.sendMinecraftPacket(response)
    }
}
