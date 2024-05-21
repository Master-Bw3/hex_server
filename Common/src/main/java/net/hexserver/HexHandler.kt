package net.hexserver

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingContext.CastSource
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.utils.putList
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.sun.net.httpserver.HttpExchange
import gay.`object`.hexdebug.adapter.DebugAdapterManager
import gay.`object`.hexdebug.debugger.CastArgs
import net.hexserver.networking.HexServerNetworking
import net.hexserver.networking.MsgDebugHexC2S
import net.hexserver.networking.MsgRunHexC2S
import net.hexserver.networking.MsgRunHexS2C
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.StringNbtReader
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import java.io.OutputStream


object HexHandlerClient {
    private var exchange: HttpExchange? = null


    fun castHex(snbt: String, response: HttpExchange) {
        this.exchange = response

        val nbt = try {
            StringNbtReader.parse(snbt)
        } catch (e: CommandSyntaxException) {
            NbtCompound()
        }
        HexServerNetworking.sendToServer(MsgRunHexC2S(nbt))
    }

    fun respond(result: List<NbtCompound>) {
        exchange?.also {
            val body = StringBuilder()
            for (iota in result) {
                body.append(HexIotaTypes.getDisplay(iota).string)
                body.append("\n")
            }

            it.sendResponseHeaders(200, body.length.toLong())
            val os: OutputStream = it.responseBody
            os.write(body.toString().toByteArray())
            os.close()

            this.exchange = null
        }
    }

    @Throws(CommandSyntaxException::class)
    fun debugHex(snbt: String) {
        val nbt = StringNbtReader.parse(snbt)

        HexServerNetworking.sendToServer(MsgDebugHexC2S(nbt))
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

        val nbtStack = NbtList()
        nbtStack.addAll(stack)

        val nbtResult = NbtCompound()
        nbtResult.putList("cast_result", nbtStack)

        HexServerNetworking.sendToPlayer(player, MsgRunHexS2C(nbtResult))
    }


    fun debugHex(nbt: NbtCompound, player: ServerPlayerEntity) {
        val world = player.getWorld()!!


        val nbtList: List<NbtElement> = nbt.getList("hexcasting:data", NbtElement.COMPOUND_TYPE.toInt())
        val instrs: MutableList<Iota> = ArrayList()
        for (nbtElement in nbtList) {
            val iota = HexIotaTypes.deserialize(nbtElement as NbtCompound, world)
            instrs.add(iota)
        }
        val sPlayer: ServerPlayerEntity = player

        val debugAdapter = DebugAdapterManager[sPlayer]

        val ctx = CastingContext(sPlayer, Hand.MAIN_HAND, CastSource.PACKAGED_HEX)

        val args = CastArgs(instrs, ctx, world) {}

        debugAdapter?.startDebugging(args)
    }
}