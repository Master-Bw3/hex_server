package net.hexserver;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.ControllerInfo;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

public class HexHandler {
    private static Iota hex;
    public static ServerPlayerEntity player;
    public static ServerWorld world;

    public static List<Iota> castHex(String snbt) {
        if (player == null || world == null) {
            return new ArrayList<>();
        }

        NbtCompound nbt;
        try {
            nbt = StringNbtReader.parse(snbt);
        } catch (CommandSyntaxException e) {
            nbt = new NbtCompound();
        }

        List<NbtElement> nbtList = nbt.getList("hexcasting:data", NbtElement.COMPOUND_TYPE);

        List<Iota> instrs = new ArrayList<>();

        for (NbtElement nbtElement : nbtList) {
            var iota = HexIotaTypes.deserialize((NbtCompound) nbtElement, world);
            instrs.add(iota);
        }

        var sPlayer = player;
        var ctx = new CastingContext(sPlayer, Hand.MAIN_HAND, CastingContext.CastSource.PACKAGED_HEX);
        var harness = new CastingHarness(ctx);
        var result = harness.executeIotas(instrs, sPlayer.getWorld());

        List<Iota> stackResult = new ArrayList<>();
        for (NbtCompound nbtCompound : result.getStack()) {
            var iota = HexIotaTypes.deserialize(nbtCompound, world);
            stackResult.add(iota);
        }

        return stackResult;
    }

    public static List<Iota> debugHex(String snbt) {
        if (player == null || world == null) {
            return new ArrayList<>();
        }

        NbtCompound nbt;
        try {
            nbt = StringNbtReader.parse(snbt);
        } catch (CommandSyntaxException e) {
            nbt = new NbtCompound();
        }

        List<NbtElement> nbtList = nbt.getList("hexcasting:data", NbtElement.COMPOUND_TYPE);

        List<Iota> instrs = new ArrayList<>();

        for (NbtElement nbtElement : nbtList) {
            var iota = HexIotaTypes.deserialize((NbtCompound) nbtElement, world);
            instrs.add(iota);
        }

        var sPlayer = player;
        var ctx = new CastingContext(sPlayer, Hand.MAIN_HAND, CastingContext.CastSource.PACKAGED_HEX);
        var harness = new CastingHarness(ctx);

//        var debugAdapter = DebugAdapterManager[player] ?: return InteractionResultHolder.fail(stack)

        var result = harness.executeIotas(instrs, sPlayer.getWorld());

        List<Iota> stackResult = new ArrayList<>();
        for (NbtCompound nbtCompound : result.getStack()) {
            var iota = HexIotaTypes.deserialize(nbtCompound, world);
            stackResult.add(iota);
        }

        return stackResult;
    }
}
