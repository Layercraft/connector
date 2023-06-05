package io.layercraft.connector

import io.layercraft.packetlib.codec.MinecraftCodec
import io.layercraft.packetlib.codec.MinecraftCodecs
import io.vertx.core.Vertx
import io.vertx.kotlin.core.vertxOptionsOf
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.runBlocking
import java.util.UUID

val SERVER_UUID: UUID = UUID.randomUUID()
val SERVER_ID = SERVER_UUID.toString().replace("-", "").substring(0, 20)
val CODEC: MinecraftCodec = MinecraftCodecs.V1_19_4
const val VERSION: String = "0.0.1"
const val MAX_PACKET_SIZE: Int = 2097151 // 3 bytes varint

fun main() {
    val vertx = Vertx.vertx(
        vertxOptionsOf(
            preferNativeTransport = true,
        )
    )

    val tcpServer = TcpServer(vertx)

    runBlocking {
        tcpServer.start()
    }

    //Shutdown hook
    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking {
            tcpServer.stop()
            vertx.close().await()
        }
    })
}
