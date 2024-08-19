package mod.master_bw3.hex_server.network

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import dev.architectury.networking.NetworkManager.PacketContext
import mod.master_bw3.hex_server.HexServer
import mod.master_bw3.hex_server.client.HexHttp.HexHttpServerManager
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import java.util.*
import java.util.function.Supplier

data class MsgEvaluateHexS2C(private val result: ExecutionClientView, private val id: UUID) : Message<Side.S2C> {
    constructor(buf: PacketByteBuf) : this(decodeExecutionClientView(buf), buf.readUuid())

    override fun encode(buf: PacketByteBuf) {
        buf.writeBoolean(result.isStackClear)
        buf.writeEnumConstant(result.resolutionType)

        buf.writeCollection(result.stackDescs, PacketByteBuf::writeNbt)
        buf.writeOptional(Optional.ofNullable(result.ravenmind), PacketByteBuf::writeNbt)
        buf.writeUuid(id)
    }

    override fun apply(supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()

        ctx.queue {
            HexHttpServerManager.hexRequestHandler?.setEvaluatedHexResult(id, result)
        }
    }

    companion object {
        private fun decodeExecutionClientView(buf: PacketByteBuf): ExecutionClientView {
            val isStackEmpty= buf.readBoolean()
            val resolutionType= buf.readEnumConstant(ResolvedPatternType::class.java)

            val stack = buf.readList(PacketByteBuf::readNbt) as List<NbtCompound>
            val raven= buf.readOptional(PacketByteBuf::readNbt).orElse(null)

            return ExecutionClientView(isStackEmpty, resolutionType, stack, raven)
        }
    }
}
