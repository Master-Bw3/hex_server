package mod.master_bw3.hex_server.network

import dev.architectury.networking.NetworkChannel
import dev.architectury.networking.NetworkManager.PacketContext
import mod.master_bw3.hex_server.HexServer
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import java.util.function.BiConsumer
import java.util.function.Supplier
import java.util.function.Function


object HexServerNetworking {
    private val CHANNEL = NetworkChannel.create(HexServer.id("networking_channel"))

    fun init() {
        register(
            MsgEvaluateHexC2S::class.java,
            MsgEvaluateHexC2S::encode,
            ::MsgEvaluateHexC2S,
            MsgEvaluateHexC2S::apply
        )
        register(
            MsgEvaluateHexS2C::class.java,
            MsgEvaluateHexS2C::encode,
            ::MsgEvaluateHexS2C,
            MsgEvaluateHexS2C::apply
        )

    }

    fun <T : Message<*>> register(
        type: Class<T>,
        encoder: BiConsumer<T, PacketByteBuf>,
        decoder: Function<PacketByteBuf, T>,
        messageConsumer: BiConsumer<T, Supplier<PacketContext>>
    ) = CHANNEL.register(type, encoder, decoder, messageConsumer)


    fun <T : Message<Side.C2S>> sendToServer(message: T) = CHANNEL.sendToServer(message)

    fun <T : Message<Side.S2C>> sendToPlayer(player: ServerPlayerEntity, message: T) =
        CHANNEL.sendToPlayer(player, message)
}

interface Message<SIDE : Side> {
    fun encode(buf: PacketByteBuf)

    fun apply(supplier: Supplier<PacketContext>)
}

sealed interface Side {
    interface C2S : Side
    interface S2C : Side
}