package net.hexserver.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.hexserver.HexServerClient;

/**
 * Fabric client loading entrypoint.
 */
public class HexServerClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HexServerClient.init();
    }
}
