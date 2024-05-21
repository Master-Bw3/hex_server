package net.hexserver.networking

import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.architectury.networking.NetworkManager.PacketContext
import gay.`object`.hexdebug.adapter.proxy.DebugProxyClient
import net.hexserver.HexHandlerClient
import net.hexserver.HexServer
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtType
import net.minecraft.network.PacketByteBuf
import java.util.function.Supplier

data class MsgRunHexS2C(private val content: NbtCompound) {
    constructor(buf: PacketByteBuf) : this(buf.readNbt()!!)

    fun encode(buf: PacketByteBuf) {
        buf.writeNbt(content)
    }

    fun apply(supplier: Supplier<PacketContext>) = supplier.get().also { ctx ->
        ctx.queue {
            HexServer.LOGGER.debug("Client received packet: {}", this)
            val castResult = content.getList("cast_result", NbtElement.COMPOUND_TYPE).map { it.asCompound }
            HexHandlerClient.setCastResult(castResult)
        }
    }
}
