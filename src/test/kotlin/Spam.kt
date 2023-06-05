
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class Spam {

    @Test
    fun `Spam test`(): Unit = runBlocking {
        val vertx = Vertx.vertx()

        (0..10000).map {
            async {
                println("Connecting $it")
                val client = vertx.createNetClient().connect(25565, "localhost").await()

                client.write("Hello world")
            }
        }.awaitAll()
    }
}
