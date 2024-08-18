package mod.master_bw3.hex_server.client.HexHttp

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import at.petrak.hexcasting.api.casting.iota.IotaType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mod.master_bw3.hex_server.HexServer
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.StringNbtReader
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

internal class HexHttpServer(val player: ClientPlayerEntity, port: Int) {
    val requestHandler = MCServerRequestHandler()

    private val server = embeddedServer(Netty, port) {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }

            post("/hexPost") {
                HexServer.LOGGER.info("get")


                val snbt = call.receiveParameters()["SNBT"]

                val hex = parseSNBT(snbt)
                if (hex == null) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respondText { "Bad Request: invalid SNBT data" }
                    return@post
                }

                val result: ExecutionClientView = try {
                    requestHandler.evaluateHex(hex).get(5, TimeUnit.SECONDS)
                } catch (e: TimeoutException) {
                    call.response.status(HttpStatusCode.InternalServerError)
                    call.respondText { "Error: Hex took too long to execute" }
                    return@post
                }

                call.response.status(HttpStatusCode.OK)
                call.respondText { result.stackDescs.map { IotaType.getDisplay(it).string }.joinToString("\n") }

            }
        }
    }.start(wait = false)


    fun stop() = server.stop()

    private fun parseSNBT(snbt: String?): NbtCompound? {
        if (snbt == null) return null
        return try {
            StringNbtReader.parse(snbt)
        } catch (e: CommandSyntaxException) {
            null
        }
    }
}