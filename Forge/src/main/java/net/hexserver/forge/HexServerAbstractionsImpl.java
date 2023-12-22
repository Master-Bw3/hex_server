package net.hexserver.forge;

import net.hexserver.HexServerAbstractions;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class HexServerAbstractionsImpl {
    /**
     * This is the actual implementation of {@link HexServerAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
