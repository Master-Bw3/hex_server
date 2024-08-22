package mod.master_bw3.hex_server.network

import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.architectury.networking.NetworkManager.PacketContext
import mod.master_bw3.hex_server.HexServer
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
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
            val castingContext = PackagedItemCastEnv(sPlayer, Hand.MAIN_HAND)
            val harness = CastingVM.empty(castingContext)
            val result = harness.queueExecuteAndWrapIotas(instrs, world)



            HexServerNetworking.sendToPlayer(player, MsgEvaluateHexS2C(result, id))
        }
    }
}
