package mod.master_bw3.hex_server


import mod.master_bw3.hex_server.network.MsgEvaluateHexC2S
import mod.master_bw3.hex_server.network.MsgEvaluateHexS2C
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.packet.CustomPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.util.TriConsumer
import java.util.function.Consumer

object FabricPacketHandler {
    fun initPackets() {
        PayloadTypeRegistry.playC2S().register(MsgEvaluateHexC2S.TYPE, MsgEvaluateHexC2S.STREAM_CODEC)
        PayloadTypeRegistry.playS2C().register(MsgEvaluateHexS2C.TYPE, MsgEvaluateHexS2C.STREAM_CODEC)
    }

    fun init() {
        ServerPlayNetworking.registerGlobalReceiver(MsgEvaluateHexC2S.TYPE, makeServerBoundHandler(MsgEvaluateHexC2S::handle))
    }

    private fun <T : CustomPayload> makeServerBoundHandler(
        handle: TriConsumer<T, MinecraftServer, ServerPlayerEntity>
    ): ServerPlayNetworking.PlayPayloadHandler<T> {
        return ServerPlayNetworking.PlayPayloadHandler { payload: T?, context: ServerPlayNetworking.Context? ->
            handle.accept(
                payload,
                context!!.server(),
                context.player()
            )
        }
    }

    fun initClient() {
        ClientPlayNetworking.registerGlobalReceiver(MsgEvaluateHexS2C.TYPE, makeClientBoundHandler(MsgEvaluateHexS2C::handle))
    }

    private fun <T : CustomPayload> makeClientBoundHandler(handler: Consumer<T>): ClientPlayNetworking.PlayPayloadHandler<T> {
        return ClientPlayNetworking.PlayPayloadHandler { payload, context -> handler.accept(payload) }
    }
}
