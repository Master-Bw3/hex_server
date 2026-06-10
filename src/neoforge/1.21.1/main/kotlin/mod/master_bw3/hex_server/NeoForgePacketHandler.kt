package mod.master_bw3.hex_server

import mod.master_bw3.hex_server.network.MsgEvaluateHexC2S
import mod.master_bw3.hex_server.network.MsgEvaluateHexS2C
import mod.master_bw3.hex_server.network.MsgType
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext
import net.neoforged.neoforge.network.handling.IPayloadHandler
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import org.apache.logging.log4j.util.TriConsumer
import java.util.function.Consumer

object NeoForgePacketHandler {

    @JvmStatic
    fun init(registrar: PayloadRegistrar) {
        registerPlayToServer(registrar, MsgEvaluateHexC2S, MsgEvaluateHexC2S::handle)
        registerPlayToClient(registrar, MsgEvaluateHexS2C, MsgEvaluateHexS2C::handle)
    }

    @JvmStatic
    private fun <T: CustomPacketPayload> registerPlayToServer(registrar: PayloadRegistrar, msgType: MsgType<T>, handler: TriConsumer<T, MinecraftServer, ServerPlayer>) {
        registrar.playToServer(msgType.TYPE, msgType.STREAM_CODEC, makeServerBoundHandler(handler))
    }

    @JvmStatic
    private fun <T : CustomPacketPayload> makeServerBoundHandler(
        handle: TriConsumer<T, MinecraftServer, ServerPlayer>
    ): IPayloadHandler<T> {
        return IPayloadHandler<T> { handler: T, ctx: IPayloadContext ->
            ctx.enqueueWork {
                handle.accept(handler, ctx.player().server!!, ctx.player() as ServerPlayer)
            }
        }
    }

    @JvmStatic
    private fun <T: CustomPacketPayload> registerPlayToClient(registrar: PayloadRegistrar, msgType: MsgType<T>, handler: Consumer<T>) {
        registrar.playToClient(msgType.TYPE, msgType.STREAM_CODEC, makeClientBoundHandler(handler))
    }

    @JvmStatic
    private fun <T : CustomPacketPayload> makeClientBoundHandler(
        handle: Consumer<T>
    ): IPayloadHandler<T> {
        return IPayloadHandler<T> { handler: T, ctx: IPayloadContext ->
            ctx.enqueueWork {
                handle.accept(handler)
            }
        }
    }
}
