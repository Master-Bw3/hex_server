package mod.master_bw3.hex_server.forge.mixin;

import mod.master_bw3.hex_server.HexServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// scuffed workaround for https://github.com/architectury/architectury-loom/issues/189
@Mixin(net.minecraft.data.Main.class)
public class MixinDatagenMain {
    @Inject(method = "main", at = @At("TAIL"), remap = false)
    private static void hex_server$systemExitAfterDatagenFinishes(String[] strings, CallbackInfo ci) {
        HexServer.LOGGER.info("Terminating datagen.");
        System.exit(0);
    }
}
