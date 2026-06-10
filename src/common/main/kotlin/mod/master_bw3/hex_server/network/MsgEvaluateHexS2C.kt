package mod.master_bw3.hex_server.network

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import mod.master_bw3.hex_server.HexServer
import mod.master_bw3.hex_server.client.hex_http.HexHttpServerManager
import net.minecraft.client.Minecraft
import net.minecraft.core.UUIDUtil
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.*

data class MsgEvaluateHexS2C(val result: ExecutionClientView, val id: UUID) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    fun handle() {
        Minecraft.getInstance().execute {
            HexHttpServerManager.hexRequestHandler?.setEvaluatedHexResult(id, result)
        }
    }

    companion object : MsgType<MsgEvaluateHexS2C> {
        override val TYPE = CustomPacketPayload.Type<MsgEvaluateHexS2C>(HexServer.id("eval_hex_sc"))

        override val STREAM_CODEC = StreamCodec.composite(
            ExecutionClientView.STREAM_CODEC, MsgEvaluateHexS2C::result,
            UUIDUtil.STREAM_CODEC, MsgEvaluateHexS2C::id,
            ::MsgEvaluateHexS2C
        )
    }
}
