package mod.master_bw3.hex_server.client

import mod.master_bw3.hex_server.FabricPacketHandler
import mod.master_bw3.hex_server.HexServerClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
object FabricHexServerClient : ClientModInitializer {
    override fun onInitializeClient() {
        HexServerClient.init()
        FabricPacketHandler.initClient()
    }
}