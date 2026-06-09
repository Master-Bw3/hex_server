package mod.master_bw3.hex_server.network

import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.xplat.IXplatAbstractions
import mod.master_bw3.hex_server.HexServer
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.Uuids
import java.util.*

data class MsgEvaluateHexC2S(val hex: List<Iota>, val id: UUID ) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = TYPE


    fun handle(server: MinecraftServer, sender: ServerPlayerEntity) {
        server.execute {
            val world = sender.world as ServerWorld

            val castingContext = PackagedItemCastEnv(sender, Hand.MAIN_HAND)
            val harness = CastingVM.empty(castingContext)
            val result = harness.queueExecuteAndWrapIotas(hex, world)

            IXplatAbstractions.INSTANCE.sendPacketToPlayer(sender, MsgEvaluateHexS2C(result, id))
        }
    }

    companion object {
        val TYPE = CustomPayload.Id<MsgEvaluateHexC2S>(HexServer.id("eval_hex_cs"))

        val STREAM_CODEC = PacketCodec.tuple(
            IotaType.TYPED_STREAM_CODEC.collect(PacketCodecs.toList()), MsgEvaluateHexC2S::hex,
            Uuids.PACKET_CODEC, MsgEvaluateHexC2S::id,
            ::MsgEvaluateHexC2S
        )
    }
}