package net.hexserver.forge;

import dev.architectury.platform.forge.EventBuses;
import net.hexserver.HexServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * This is your loading entrypoint on forge, in case you need to initialize
 * something platform-specific.
 */
@Mod(HexServer.MOD_ID)
public class HexServerForge {
    public HexServerForge() {
        // Submit our event bus to let architectury register our content on the right time
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(HexServer.MOD_ID, bus);
        bus.addListener(HexServerClientForge::init);
        HexServer.init();
    }
}
