package mod.master_bw3.hex_server.fabric

import mod.master_bw3.hex_server.FabricPacketHandler
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object FabricHexServer : ModInitializer {
    const val MODID = "hex_server"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    fun id(path: String) = Identifier.of(MODID, path)

    override fun onInitialize() {
        FabricPacketHandler.initPackets()
        FabricPacketHandler.init()
    }
}
