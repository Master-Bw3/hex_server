package net.hexserver.networking

import dev.architectury.networking.NetworkChannel
import net.hexserver.HexServer
import net.minecraft.server.network.ServerPlayerEntity

object HexServerNetworking {
    private val CHANNEL = NetworkChannel.create(HexServer.id("networking_channel"))

    fun init() {
        CHANNEL.register(MsgRunHexC2S::class.java, MsgRunHexC2S::encode, ::MsgRunHexC2S, MsgRunHexC2S::apply)
        CHANNEL.register(MsgRunHexS2C::class.java, MsgRunHexS2C::encode, ::MsgRunHexS2C, MsgRunHexS2C::apply)
        CHANNEL.register(MsgDebugHexC2S::class.java, MsgDebugHexC2S::encode, ::MsgDebugHexC2S, MsgDebugHexC2S::apply)
    }

    fun <T> sendToServer(message: T) = CHANNEL.sendToServer(message)

    fun <T> sendToPlayer(player: ServerPlayerEntity, message: T) = CHANNEL.sendToPlayer(player, message)
}
