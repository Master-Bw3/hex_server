package net.hexserver

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingContext.CastSource
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.utils.putList
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import com.mojang.brigadier.exceptions.CommandSyntaxException
import gay.`object`.hexdebug.adapter.DebugAdapterManager
import gay.`object`.hexdebug.debugger.CastArgs
import net.hexserver.HexServer.LOGGER
import net.hexserver.networking.HexServerNetworking
import net.hexserver.networking.MsgDebugHexC2S
import net.hexserver.networking.MsgRunHexC2S
import net.hexserver.networking.MsgRunHexS2C
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.StringNbtReader
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult


object HexHandlerClient {
    private var castResult: List<NbtCompound>? = null

    fun castHex(snbt: String) {
        val nbt = try {
            StringNbtReader.parse(snbt)
        } catch (e: CommandSyntaxException) {
            NbtCompound()
        }
        HexServerNetworking.sendToServer(MsgRunHexC2S(nbt))
    }

    @Throws(CommandSyntaxException::class)
    fun debugHex(snbt: String)  {
        val nbt = StringNbtReader.parse(snbt)

        HexServerNetworking.sendToServer(MsgDebugHexC2S(nbt))
    }


    fun setCastResult(result: List<NbtCompound>) {
        this.castResult = result;

    }

    fun takeCastResult(): List<NbtCompound>? {
        val result = this.castResult;
        this.castResult = null;
        return result;
    }
}

object HexHandlerServer {

    fun castHex(nbt: NbtCompound, player: ServerPlayerEntity) {
        val world = player.getWorld()

        val nbtList: List<NbtElement> = nbt.getList("hexcasting:data", NbtElement.COMPOUND_TYPE.toInt())
        val instrs: MutableList<Iota> = ArrayList()
        for (nbtElement in nbtList) {
            val iota = HexIotaTypes.deserialize(nbtElement as NbtCompound, world)
            instrs.add(iota)
        }
        val sPlayer: ServerPlayerEntity = player
        val ctx = CastingContext(sPlayer, Hand.MAIN_HAND, CastSource.PACKAGED_HEX)
        val harness = CastingHarness(ctx)
        val (_, _, stack) = harness.executeIotas(instrs, sPlayer.getWorld())

        val nbtStack = NbtList();
        nbtStack.addAll(stack)

        val nbtResult = NbtCompound();
        nbtResult.putList("cast_result", nbtStack)

        HexServerNetworking.sendToPlayer(player, MsgRunHexS2C(nbtResult))
    }



    fun debugHex(nbt: NbtCompound, player: ServerPlayerEntity) {
        val world = player.getWorld()


        val nbtList: List<NbtElement> = nbt.getList("hexcasting:data", NbtElement.COMPOUND_TYPE.toInt())
        val instrs: MutableList<Iota> = ArrayList()
        for (nbtElement in nbtList) {
            val iota = HexIotaTypes.deserialize(nbtElement as NbtCompound, world)
            instrs.add(iota)
        }
        val sPlayer: ServerPlayerEntity = player

        val debugAdapter = DebugAdapterManager[sPlayer]!!

        val ctx = CastingContext(sPlayer, Hand.MAIN_HAND, CastSource.PACKAGED_HEX)

        val args = CastArgs(instrs, ctx, world!!) {}

        debugAdapter.startDebugging(args)
    }
}