package mod.master_bw3.hex_server.network

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

interface MsgType<T : CustomPacketPayload> {
    val TYPE: CustomPacketPayload.Type<T>
    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, T>
}