package mod.master_bw3.hex_server.client.HexHttp

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import at.petrak.hexcasting.api.casting.iota.IotaType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import dev.architectury.platform.Platform
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mod.master_bw3.hex_server.network.HexServerNetworking
import mod.master_bw3.hex_server.network.MsgDebugHexC2S
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.StringNbtReader
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

internal class HexHttpServer(val player: ClientPlayerEntity, port: Int) {
    val hexRequestHandler = EvalHexRequestHandler()

    private val server = embeddedServer(Jetty, port) {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }

            post("/hexPost") {
                val snbt = call.receiveParameters()["SNBT"]

                val hex = parseSNBT(snbt)
                if (hex == null) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respondText { "Bad Request: invalid SNBT data" }
                    return@post
                }

                val result: ExecutionClientView = try {
                    hexRequestHandler.evaluateHex(hex).get()
                } catch (e: TimeoutException) {
                    call.response.status(HttpStatusCode.InternalServerError)
                    call.respondText { "Error: Hex took too long to execute" }
                    return@post
                }

                call.response.status(HttpStatusCode.OK)
                call.respondText { result.stackDescs.joinToString("\n") { IotaType.getDisplay(it).string } }

            }

            post("/hexDebug") {
                if (!Platform.isModLoaded("hexdebug")) {
                    call.response.status(HttpStatusCode.NotImplemented)
                    call.respondText { "Server Error: HexDebug is not installed" }
                    return@post
                }

                val snbt = call.receiveParameters()["SNBT"]

                val hex = parseSNBT(snbt)
                if (hex == null) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respondText { "Bad Request: invalid SNBT data" }
                    return@post
                }

                HexServerNetworking.sendToServer(MsgDebugHexC2S(hex))

                call.response.status(HttpStatusCode.OK)
                call.respondText { "Hex successfully received for debugging" }
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