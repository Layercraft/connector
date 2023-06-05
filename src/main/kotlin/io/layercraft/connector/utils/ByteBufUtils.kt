package io.layercraft.connector.utils

import io.netty.buffer.ByteBuf

internal fun ByteBuf.readVarInt(): Int {
    var i = 0
    var j = 0
    while (true) {
        val k = readByte().toInt()
        i = i or (k and 127 shl j++ * 7)
        if (j > 5) {
            throw RuntimeException("VarInt too big")
        }
        if (k and 128 != 128) {
            break
        }
    }
    return i
}

internal fun ByteBuf.writeVarInt(value: Int) {
    var i = value
    while (true) {
        if (i and -128 == 0) {
            writeByte(i)
            return
        }
        writeByte(i and 127 or 128)
        i = i ushr 7
    }
}


internal fun ByteBuf.remaining(): Int {
    return this.maxCapacity() - this.readerIndex()
}

internal fun ByteBuf.hasRemaining(): Boolean {
    return this.readerIndex() < this.maxCapacity()
}
