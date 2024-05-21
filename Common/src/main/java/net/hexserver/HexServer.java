package net.hexserver;

import com.sun.net.httpserver.HttpServer;
import net.hexserver.networking.HexServerNetworking;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * This is effectively the loading entrypoint for most of your code, at least
 * if you are using Architectury as intended.
 */
public class HexServer {
    public static final String MOD_ID = "hexserver";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    public static void init() {
        LOGGER.info("Hex Server says hello!");

        LOGGER.info(HexServerAbstractions.getConfigDirectory().toAbsolutePath().normalize().toString());

        HexServerNetworking.INSTANCE.init();
    }





    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static Identifier id(String string) {
        return new Identifier(MOD_ID, string);
    }
}
