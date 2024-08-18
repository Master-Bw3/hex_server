package mod.master_bw3.hex_server.network

import dev.architectury.networking.NetworkManager.PacketContext
import mod.master_bw3.hex_server.HexServer
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import java.util.function.Supplier

data class MsgEvaluateHexS2C(private val hex: NbtCompound) : Message<Side.S2C> {
    constructor(buf: PacketByteBuf) : this(buf.readNbt()!!)

    override fun encode(buf: PacketByteBuf) {
        buf.writeNbt(hex)
    }

    override fun apply(supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()

        ctx.queue {
            HexServer.LOGGER.info("packet moment")
        }
    }
}
