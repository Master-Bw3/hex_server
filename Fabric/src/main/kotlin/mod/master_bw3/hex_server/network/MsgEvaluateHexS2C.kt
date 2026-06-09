package mod.master_bw3.hex_server.network

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import mod.master_bw3.hex_server.client.HexHttp.HexHttpServerManager
import mod.master_bw3.hex_server.fabric.FabricHexServer
import net.minecraft.client.MinecraftClient
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Uuids
import java.util.UUID

data class MsgEvaluateHexS2C(val result: ExecutionClientView, val id: UUID) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = TYPE


    fun handle() {
        MinecraftClient.getInstance().execute {
            HexHttpServerManager.hexRequestHandler?.setEvaluatedHexResult(id, result)
        }
    }


    companion object {
        val TYPE = CustomPayload.Id<MsgEvaluateHexS2C>(FabricHexServer.id("eval_hex_sc"))

        val STREAM_CODEC = PacketCodec.tuple(
            ExecutionClientView.STREAM_CODEC, MsgEvaluateHexS2C::result,
            Uuids.PACKET_CODEC, MsgEvaluateHexS2C::id,
            ::MsgEvaluateHexS2C
        )
    }
}
