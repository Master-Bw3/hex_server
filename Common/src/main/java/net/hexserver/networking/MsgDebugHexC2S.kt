package net.hexserver.networking

import dev.architectury.networking.NetworkManager.PacketContext
import net.hexserver.HexHandlerServer
import net.hexserver.HexServer
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import java.util.function.Supplier

data class MsgDebugHexC2S(private val content: NbtCompound) {
    constructor(buf: PacketByteBuf) : this(buf.readNbt()!!)

    fun encode(buf: PacketByteBuf) {
        buf.writeNbt(content)
    }

    fun apply(supplier: Supplier<PacketContext>) = supplier.get().also { ctx ->
        ctx.queue {
            HexServer.LOGGER.debug("Server received packet from {}: {}", ctx.player.name.string, this)
            HexHandlerServer.debugHex(content, ctx.player as ServerPlayerEntity)

        }
    }
}
