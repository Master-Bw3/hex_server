package net.hexserver.forge;

import net.hexserver.HexServerClient;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Forge client loading entrypoint.
 */
public class HexServerClientForge {
    public static void init(FMLClientSetupEvent event) {
        HexServerClient.init();
    }
}
