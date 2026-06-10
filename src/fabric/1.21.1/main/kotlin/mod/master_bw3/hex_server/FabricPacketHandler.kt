package mod.master_bw3.hex_server


import mod.master_bw3.hex_server.network.MsgEvaluateHexC2S
import mod.master_bw3.hex_server.network.MsgEvaluateHexS2C
import mod.master_bw3.hex_server.network.MsgType
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import org.apache.logging.log4j.util.TriConsumer
import java.util.function.Consumer

object FabricPacketHandler {
    fun initPackets() {
        registerC2S(MsgEvaluateHexC2S)
        registerS2C(MsgEvaluateHexS2C)
    }

    fun init() {
        registerServerReciever(MsgEvaluateHexC2S, MsgEvaluateHexC2S::handle)
    }

    fun initClient() {
        registerClientReciever(MsgEvaluateHexS2C, MsgEvaluateHexS2C::handle)
    }

    private fun <T: CustomPacketPayload> registerC2S(msgType: MsgType<T>) {
        PayloadTypeRegistry.playC2S().register(msgType.TYPE, msgType.STREAM_CODEC)
    }

    private fun <T : CustomPacketPayload> registerServerReciever(msg: MsgType<T>, handler: TriConsumer<T, MinecraftServer, ServerPlayer> ) {
        ServerPlayNetworking.registerGlobalReceiver(msg.TYPE, makeServerBoundHandler(handler))
    }

    private fun <T : CustomPacketPayload> makeServerBoundHandler(
        handle: TriConsumer<T, MinecraftServer, ServerPlayer>
    ): ServerPlayNetworking.PlayPayloadHandler<T> {
        return ServerPlayNetworking.PlayPayloadHandler { payload: T?, context: ServerPlayNetworking.Context? ->
            handle.accept(
                payload,
                context!!.server(),
                context.player()
            )
        }
    }

    private fun <T: CustomPacketPayload> registerS2C(msgType: MsgType<T>) {
        PayloadTypeRegistry.playS2C().register(msgType.TYPE, msgType.STREAM_CODEC)
    }

    private fun <T : CustomPacketPayload> registerClientReciever(msg: MsgType<T>, handler: Consumer<T> ) {
        ClientPlayNetworking.registerGlobalReceiver(msg.TYPE, makeClientBoundHandler(handler))
    }

    private fun <T : CustomPacketPayload> makeClientBoundHandler(handler: Consumer<T>): ClientPlayNetworking.PlayPayloadHandler<T> {
        return ClientPlayNetworking.PlayPayloadHandler { payload, context -> handler.accept(payload) }
    }
}
