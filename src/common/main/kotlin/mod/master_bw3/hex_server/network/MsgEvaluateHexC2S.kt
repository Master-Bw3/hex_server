package mod.master_bw3.hex_server.network

import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import mod.master_bw3.hex_server.HexServer
import mod.master_bw3.hex_server.sendPacketToPlayer
import net.minecraft.core.UUIDUtil
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import java.util.*

data class MsgEvaluateHexC2S(val hex: List<Iota>, val id: UUID) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    fun handle(server: MinecraftServer, sender: ServerPlayer) {
        server.execute {
            val world = sender.level() as ServerLevel

            val castingContext = PackagedItemCastEnv(sender, InteractionHand.MAIN_HAND)
            val harness = CastingVM.empty(castingContext)
            val result = harness.queueExecuteAndWrapIotas(hex, world)

            sendPacketToPlayer(sender, MsgEvaluateHexS2C(result, id))
        }
    }

    companion object : MsgType<MsgEvaluateHexC2S> {
        override val TYPE = CustomPacketPayload.Type<MsgEvaluateHexC2S>(HexServer.id("eval_hex_cs"))

        override val STREAM_CODEC = StreamCodec.composite(
            IotaType.TYPED_STREAM_CODEC.apply(ByteBufCodecs.list()), MsgEvaluateHexC2S::hex,
            UUIDUtil.STREAM_CODEC, MsgEvaluateHexC2S::id,
            ::MsgEvaluateHexC2S
        )
    }
}