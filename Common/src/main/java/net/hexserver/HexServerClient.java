package net.hexserver;

import dev.architectury.event.events.client.ClientPlayerEvent;
import net.hexserver.server.Server;


/**
 * Common client loading entrypoint.
 */
public class HexServerClient {
    static Server httpServer;

    public static void init() {

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register((x) -> {
            httpServer = new Server();
        });


        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((x) -> {
            if (httpServer != null) {
                httpServer.stop();
            }
        });
    }
}
