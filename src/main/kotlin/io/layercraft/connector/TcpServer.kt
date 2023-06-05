package io.layercraft.connector

import io.layercraft.connector.handler.Handler
import io.layercraft.connector.packets.Translator
import io.layercraft.connector.utils.Connection
import io.layercraft.connector.utils.ConnectionUtils
import io.layercraft.connector.utils.hasRemaining
import io.layercraft.connector.utils.readVarInt
import io.layercraft.connector.utils.remaining
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.vertx.core.Vertx
import io.vertx.core.net.NetServer
import io.vertx.core.net.NetSocket
import io.vertx.kotlin.core.net.netServerOptionsOf
import io.vertx.kotlin.coroutines.await
import java.util.logging.Logger

class TcpServer(
    private val vertx: Vertx,
) {

    private val logger: Logger = Logger.getLogger(TcpServer::class.java.name)
    private val netServer: NetServer = vertx.createNetServer(
        netServerOptionsOf(
            port = 25565,
        )
    )

    suspend fun start() {
        netServer.connectHandler { socket ->
            handleConnection(socket)
        }.listen().await()
        logger.info("Server started with id $SERVER_UUID, using native transport: ${vertx.isNativeTransportEnabled}")
    }


    suspend fun stop() {
        netServer.close().await()
        logger.info("Server stopped with id $SERVER_UUID")
    }

    private fun handleConnection(socket: NetSocket) {
        val registeredConnection = ConnectionUtils.registerConnection(socket)

        socket.closeHandler {
            handleDisconnect(socket)
        }

        socket.handler {
            handleInput(
                connection = registeredConnection,
                buffer = it.byteBuf,
                socket = socket
            )
        }
    }

    private fun handleDisconnect(socket: NetSocket) {
        ConnectionUtils.removeConnection(socket)
    }

    private fun handleInput(buffer: ByteBuf, socket: NetSocket, connection: Connection) {
        loop@ do {
            if (!buffer.hasRemaining()) break@loop
            val packetLength = buffer.readVarInt()
            if (packetLength > buffer.remaining()) {
                logger.warning("Received packet with length $packetLength but only ${buffer.remaining()} bytes are available")
                socket.close()
                return
            }
            val packetArray = buffer.readBytes(packetLength).array()
            handlePacket(Unpooled.wrappedBuffer(packetArray), socket, connection)
        } while (buffer.hasRemaining())
        buffer.clear()
    }

    private fun handlePacket(buffer: ByteBuf, socket: NetSocket, connection: Connection) {
        val packetState = connection.packetState
        val packet = Translator.deserialize(packetState, buffer)
        if (packet == null) {
            logger.warning("Received unknown packet $packetState")
            socket.close()
            return
        }
        Handler.handle(packet, connection)
    }
}
