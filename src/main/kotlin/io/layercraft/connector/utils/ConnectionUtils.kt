package io.layercraft.connector.utils

import io.layercraft.packetlib.packets.PacketState
import io.vertx.core.net.NetSocket
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.SecretKey

object ConnectionUtils {

    private val connectionIds = mutableMapOf<String, UUID>()
    private val connections = mutableMapOf<UUID, Connection>()


    fun registerConnection(socket: NetSocket): Connection {
        val uuid = UUID.randomUUID()
        connectionIds[socket.writeHandlerID()] = uuid
        connections[uuid] = Connection(uuid, socket)
        return connections[uuid]!!
    }

    fun removeConnection(socket: NetSocket) {
        val uuid = connectionIds[socket.writeHandlerID()] ?: return
        connectionIds.remove(socket.writeHandlerID())
        connections.remove(uuid)
    }

    fun getConnection(socket: NetSocket): Connection? {
        val uuid = connectionIds[socket.writeHandlerID()] ?: return null
        return connections[uuid]
    }

    fun getConnection(uuid: UUID): Connection? {
        return connections[uuid]
    }

    fun getConnection(id: String): Connection? {
        val uuid = connectionIds[id] ?: return null
        return connections[uuid]
    }
}

data class Connection(
    val uuid: UUID,
    val socket: NetSocket,
    var packetState: PacketState = PacketState.HANDSHAKING,
    val verifyToken: ByteArray = EncryptionUtils.generateVerifyToken(),
    var sharedSecret: SecretKey? = null,
    var username: String? = null,
    var mcUUID: UUID? = null,
    var host: String? = null,
    var cipherContext: CipherContext? = null,
)

data class CipherContext(
    val decrypt: Cipher,
    val encrypt: Cipher,
)
