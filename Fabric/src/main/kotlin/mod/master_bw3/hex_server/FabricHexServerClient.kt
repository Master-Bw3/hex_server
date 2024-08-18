package mod.master_bw3.hex_server.fabric

import mod.master_bw3.hex_server.HexServerClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType.CLIENT
import net.fabricmc.api.Environment

@Environment(CLIENT)
object FabricHexServerClient : ClientModInitializer {
    override fun onInitializeClient() {
        HexServerClient.init()
    }
}
