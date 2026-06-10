package mod.master_bw3.hex_server.client.hex_http

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.jakarta.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.minecraft.client.player.LocalPlayer
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.TagParser
import java.util.concurrent.TimeoutException
import kotlin.jvm.optionals.getOrNull

internal class HexHttpServer(val player: LocalPlayer, port: Int) {
    val hexRequestHandler = EvalHexRequestHandler()

    private val server = embeddedServer(Jetty, port) {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }

            post("/hexPost") {
                val snbt = call.receiveParameters()["SNBT"]

                val hex = parseSNBT(snbt)
                    ?.let { REQUEST_CODEC.decode(NbtOps.INSTANCE, it).resultOrPartial() }
                    ?.getOrNull()
                    ?.first
                    ?.hex

                if (hex == null) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respondText { "Bad Request: invalid NBT data" }
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
                call.respondText { result.stackDescs.joinToString("\n") { it.display().string } }

            }

//            post("/hexDebug") {
//                if (!Platform.isModLoaded("hexdebug")) {
//                    call.response.status(HttpStatusCode.NotImplemented)
//                    call.respondText { "Server Error: HexDebug is not installed" }
//                    return@post
//                }
//
//                val snbt = call.receiveParameters()["SNBT"]
//
//                val hex = parseSNBT(snbt)
//                if (hex == null) {
//                    call.response.status(HttpStatusCode.BadRequest)
//                    call.respondText { "Bad Request: invalid SNBT data" }
//                    return@post
//                }
//
//                IClientXplatAbstractions.INSTANCE.sendPacketToServer(MsgDebugHexC2S(hex))
//
//                call.response.status(HttpStatusCode.OK)
//                call.respondText { "Hex successfully received for debugging" }
//            }
        }
    }.start(wait = false)


    fun stop() = server.stop()

    private fun parseSNBT(snbt: String?): CompoundTag? {
        if (snbt == null) return null
        return try {
            TagParser.parseTag(snbt)
        } catch (e: CommandSyntaxException) {
            null
        }
    }
}

data class RequestHex(val hex: List<Iota>)

val REQUEST_CODEC = RecordCodecBuilder.create { instance ->
    instance.group(IotaType.TYPED_CODEC.listOf().fieldOf("hex").forGetter(RequestHex::hex))
        .apply(instance, ::RequestHex)
}
