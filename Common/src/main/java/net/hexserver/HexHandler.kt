package net.hexserver

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingContext.CastSource
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import com.mojang.brigadier.exceptions.CommandSyntaxException
import gay.`object`.hexdebug.adapter.DebugAdapterManager
import gay.`object`.hexdebug.debugger.CastArgs
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.StringNbtReader
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult


object HexHandler {
    private val hex: Iota? = null
    var player: ServerPlayerEntity? = null
    var world: ServerWorld? = null

    fun castHex(snbt: String?): List<Iota?>? {
        if (player == null || world == null) {
            return ArrayList()
        }
        val nbt: NbtCompound
        nbt = try {
            StringNbtReader.parse(snbt)
        } catch (e: CommandSyntaxException) {
            NbtCompound()
        }
        val nbtList: List<NbtElement> = nbt.getList("hexcasting:data", NbtElement.COMPOUND_TYPE.toInt())
        val instrs: MutableList<Iota> = ArrayList()
        for (nbtElement in nbtList) {
            val iota = HexIotaTypes.deserialize(nbtElement as NbtCompound, world)
            instrs.add(iota)
        }
        val sPlayer: ServerPlayerEntity = player!!
        val ctx = CastingContext(sPlayer, Hand.MAIN_HAND, CastSource.PACKAGED_HEX)
        val harness = CastingHarness(ctx)
        val (_, _, stack) = harness.executeIotas(instrs, sPlayer.getWorld())
        val stackResult: MutableList<Iota?> = ArrayList()
        for (nbtCompound in stack) {
            val iota = HexIotaTypes.deserialize(nbtCompound, world)
            stackResult.add(iota)
        }
        return stackResult
    }

    fun debugHex(snbt: String?): List<Iota?>? {
        if (player == null || world == null) {
            return ArrayList()
        }
        val nbt: NbtCompound
        nbt = try {
            StringNbtReader.parse(snbt)
        } catch (e: CommandSyntaxException) {
            NbtCompound()
        }
        val nbtList: List<NbtElement> = nbt.getList("hexcasting:data", NbtElement.COMPOUND_TYPE.toInt())
        val instrs: MutableList<Iota> = ArrayList()
        for (nbtElement in nbtList) {
            val iota = HexIotaTypes.deserialize(nbtElement as NbtCompound, world)
            instrs.add(iota)
        }
        val sPlayer: ServerPlayerEntity = player!!

        val debugAdapter = DebugAdapterManager[sPlayer] ?: return ArrayList()

        val ctx = CastingContext(sPlayer, Hand.MAIN_HAND, CastSource.PACKAGED_HEX)

        val args = CastArgs(instrs, ctx, world!!) {}

        debugAdapter.startDebugging(args)

        //val (_, _, stack) = harness.executeIotas(instrs, sPlayer.getWorld())


        val stackResult: MutableList<Iota?> = ArrayList()
//        for (nbtCompound in stack) {
//            val iota = HexIotaTypes.deserialize(nbtCompound, world)
//            stackResult.add(iota)
//        }
        return stackResult
    }
}