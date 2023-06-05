package io.layercraft.connector.packets

import io.layercraft.packetlib.serialization.MinecraftProtocolSerializeInterface
import io.netty.buffer.ByteBuf

class ByteBufSerializer(override val output: ByteBuf) : MinecraftProtocolSerializeInterface<ByteBuf> {
    override fun writeBoolean(input: Boolean) {
        output.writeBoolean(input)
    }

    override fun writeByte(input: Byte) {
        output.writeByte(input.toInt())
    }

    override fun writeBytes(input: ByteArray) {
        output.writeBytes(input)
    }

    override fun writeDouble(input: Double) {
        output.writeDouble(input)
    }

    override fun writeFloat(input: Float) {
        output.writeFloat(input)
    }

    override fun writeInt(input: Int) {
        output.writeInt(input)
    }

    override fun writeLong(input: Long) {
        output.writeLong(input)
    }

    override fun writeShort(input: Short) {
        output.writeShort(input.toInt())
    }

    override fun writeUByte(input: UByte) {
        output.writeByte(input.toInt())
    }

    override fun writeUShort(input: UShort) {
        output.writeShort(input.toInt())
    }
}
