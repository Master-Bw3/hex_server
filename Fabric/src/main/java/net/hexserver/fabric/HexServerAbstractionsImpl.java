package net.hexserver.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.hexserver.HexServerAbstractions;

import java.nio.file.Path;

public class HexServerAbstractionsImpl {
    /**
     * This is the actual implementation of {@link HexServerAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
