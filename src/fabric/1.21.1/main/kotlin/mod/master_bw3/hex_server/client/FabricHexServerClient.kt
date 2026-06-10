package mod.master_bw3.hex_server.client

import net.fabricmc.api.ClientModInitializer

class FabricHexServerClient : ClientModInitializer {
    override fun onInitializeClient() {
        println("Hello Fabric Client World")
    }
}