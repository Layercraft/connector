package io.layercraft.connector.packets

import io.layercraft.connector.MAX_PACKET_SIZE
import io.layercraft.packetlib.codec.MinecraftCodecs
import io.layercraft.packetlib.packets.Packet
import io.layercraft.packetlib.packets.PacketState
import io.netty.buffer.ByteBuf
import io.vertx.core.buffer.impl.VertxByteBufAllocator

object Translator {

    private val codec = MinecraftCodecs.V1_19_3

    fun deserialize(packetState: PacketState, buffer: ByteBuf): Packet? {
        val deserializer = ByteBufDeserializer(buffer)
        val packetId = deserializer.readVarInt()
        val packetSerializer = codec.getServerBoundCodecPacket(packetState, packetId)?.packetSerializer
        val packet = packetSerializer?.deserialize(deserializer)

        buffer.release()

        return packet
    }

    fun serialize(packet: Packet): ByteBuf {
        val buffer = VertxByteBufAllocator.POOLED_ALLOCATOR.heapBuffer(256, MAX_PACKET_SIZE)
        val serializer = ByteBufSerializer(buffer)
        val codecPacketFromPacket = codec.getCodecPacketFromPacket(packet)!!
        serializer.writeVarInt(codecPacketFromPacket.packetId)
        codecPacketFromPacket.packetSerializer.serialize(serializer, packet)
        return serializer.output
    }
}
