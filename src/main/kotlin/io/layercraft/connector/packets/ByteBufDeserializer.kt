package io.layercraft.connector.packets

import io.layercraft.connector.utils.remaining
import io.layercraft.packetlib.serialization.MinecraftProtocolDeserializeInterface
import io.netty.buffer.ByteBuf

class ByteBufDeserializer(override val input: ByteBuf) : MinecraftProtocolDeserializeInterface<ByteBuf> {
    override val remaining: Int
        get() = input.remaining()

    override fun readBoolean(): Boolean = input.readBoolean()

    override fun readByte(): Byte = input.readByte()

    override fun readBytes(): ByteArray = input.readBytes(input.readableBytes()).array()

    override fun readBytes(n: Int): ByteArray{
        val array = ByteArray(n)
        input.readBytes(array)
        return array
    }

    override fun readDouble(): Double = input.readDouble()

    override fun readFloat(): Float = input.readFloat()

    override fun readInt(): Int = input.readInt()

    override fun readLong(): Long = input.readLong()

    override fun readShort(): Short = input.readShort()

    override fun readUByte(): UByte = input.readUnsignedByte().toUByte()

    override fun readUShort(): UShort = input.readUnsignedShort().toUShort()
}
