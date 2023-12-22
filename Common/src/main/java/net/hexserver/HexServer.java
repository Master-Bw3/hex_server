package net.hexserver;

import net.hexserver.registry.HexServerIotaTypeRegistry;
import net.hexserver.registry.HexServerItemRegistry;
import net.hexserver.registry.HexServerPatternRegistry;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is effectively the loading entrypoint for most of your code, at least
 * if you are using Architectury as intended.
 */
public class HexServer {
    public static final String MOD_ID = "hexserver";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    public static void init() {
        LOGGER.info("Hex Server says hello!");

        HexServerItemRegistry.init();
        HexServerIotaTypeRegistry.init();
        HexServerPatternRegistry.init();

        LOGGER.info(HexServerAbstractions.getConfigDirectory().toAbsolutePath().normalize().toString());
    }

    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static Identifier id(String string) {
        return new Identifier(MOD_ID, string);
    }
}
