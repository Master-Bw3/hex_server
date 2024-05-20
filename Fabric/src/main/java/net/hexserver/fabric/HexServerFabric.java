package net.hexserver.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hexserver.HexHandler;
import net.hexserver.HexServer;
/**
 * This is your loading entrypoint on fabric(-likes), in case you need to initialize
 * something platform-specific.
 * <br/>
 * Since quilt can load fabric mods, you develop for two platforms in one fell swoop.
 * Feel free to check out the <a href="https://github.com/architectury/architectury-templates">Architectury templates</a>
 * if you want to see how to add quilt-specific code.
 */
public class HexServerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HexServer.init();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            HexHandler.INSTANCE.setPlayer(handler.player);

            HexHandler.INSTANCE.setWorld(server.getOverworld());

            HexServer.LOGGER.info(handler.player);
            HexServer.LOGGER.info(server.getOverworld());
            HexServer.LOGGER.info("world and player added");
        });
    }
}
