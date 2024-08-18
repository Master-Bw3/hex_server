package mod.master_bw3.hex_server.network

import dev.architectury.networking.NetworkManager.PacketContext
import mod.master_bw3.hex_server.HexServer
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import java.util.UUID
import java.util.function.Supplier

data class MsgEvaluateHexC2S(private val hex: NbtCompound, private val id: UUID) : Message<Side.C2S> {
    constructor(buf: PacketByteBuf) : this(buf.readNbt()!!, buf.readUuid())

    override fun encode(buf: PacketByteBuf) {
        buf.writeNbt(hex)
        buf.writeUuid(id)
    }

    override fun apply(supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()

        ctx.queue {
            HexServer.LOGGER.info("packet moment")
        }
    }
}
