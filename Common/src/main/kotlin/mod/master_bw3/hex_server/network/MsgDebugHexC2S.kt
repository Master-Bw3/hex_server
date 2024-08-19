package mod.master_bw3.hex_server.network

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import dev.architectury.networking.NetworkManager.PacketContext
import gay.`object`.hexdebug.adapter.DebugAdapterManager
import gay.`object`.hexdebug.debugger.CastArgs
import mod.master_bw3.hex_server.HexServer
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import java.util.function.Supplier
import gay.`object`.hexdebug.casting.eval.DebuggerCastEnv


data class MsgDebugHexC2S(private val hex: NbtCompound) : Message<Side.C2S> {
    constructor(buf: PacketByteBuf) : this(buf.readNbt()!!)

    override fun encode(buf: PacketByteBuf) {
        buf.writeNbt(hex)
    }

    override fun apply(supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()
        val player = ctx.player
        player.server!!.execute {
            val world = player.world as ServerWorld

            val nbtList: List<NbtElement> = hex.getList("hexcasting:data", NbtElement.COMPOUND_TYPE.toInt())
            val instrs: MutableList<Iota> = ArrayList()
            for (nbtElement in nbtList) {
                val iota = IotaType.deserialize(nbtElement as NbtCompound, world)
                instrs.add(iota)
            }
            val sPlayer: ServerPlayerEntity = player as ServerPlayerEntity
            val castingContext = DebuggerCastEnv(sPlayer, Hand.MAIN_HAND)
            val args = CastArgs(instrs, castingContext, world) {}

            val debugAdapter = DebugAdapterManager[sPlayer]

            debugAdapter?.terminate(null)
            debugAdapter?.startDebugging(args)
        }
    }
}
