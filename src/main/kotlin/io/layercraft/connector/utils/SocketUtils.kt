package io.layercraft.connector.utils

import io.layercraft.connector.MAX_PACKET_SIZE
import io.layercraft.connector.packets.Translator
import io.layercraft.packetlib.packets.Packet
import io.vertx.core.buffer.impl.VertxByteBufAllocator
import io.vertx.core.net.NetSocket
import io.vertx.core.net.impl.NetSocketInternal

internal fun NetSocket.sendMinecraftPacket(packet: Packet) {
    val buffer = Translator.serialize(packet)

    val finishedBuffer = VertxByteBufAllocator.POOLED_ALLOCATOR.heapBuffer(buffer.readableBytes() + 5, MAX_PACKET_SIZE)

    finishedBuffer.writeVarInt(buffer.readableBytes())
    finishedBuffer.writeBytes(buffer)

    val cipherContext = ConnectionUtils.getConnection(this)?.cipherContext
    cipherContext?.encrypt?.update(finishedBuffer.array(), finishedBuffer.arrayOffset(), finishedBuffer.readableBytes(), finishedBuffer.array(), finishedBuffer.arrayOffset())

    (this as NetSocketInternal).writeMessage(finishedBuffer).andThen {
        finishedBuffer.release()
        buffer.release()
    }
}

internal fun Connection.sendMinecraftPacket(packet: Packet) {
    this.socket.sendMinecraftPacket(packet)
}
