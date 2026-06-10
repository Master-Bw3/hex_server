package mod.master_bw3.hex_server

import mod.master_bw3.hex_server.FabricPacketHandler
import mod.master_bw3.hex_server.HexServer
import net.fabricmc.api.ModInitializer

object FabricHexServer : ModInitializer {
    override fun onInitialize() {
        HexServer.init()
        FabricPacketHandler.initPackets()
        FabricPacketHandler.init()
    }
}
