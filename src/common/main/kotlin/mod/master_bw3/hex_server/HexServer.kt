package mod.master_bw3.hex_server

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object HexServer {
    const val MODID = "hex_server"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    fun id(path: String) = ResourceLocation.fromNamespaceAndPath(MODID, path)

    fun init() {

    }
}
