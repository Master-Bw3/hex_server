package mod.master_bw3.hex_server

import mod.master_bw3.hex_server.network.HexServerNetworking
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object HexServer {
    const val MODID = "hexui"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    fun id(path: String) = Identifier(MODID, path)

    fun init() {
        HexServerNetworking.init()
    }
}
