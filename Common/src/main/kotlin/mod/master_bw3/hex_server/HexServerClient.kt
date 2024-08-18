package mod.master_bw3.hex_server

import mod.master_bw3.hex_server.client.HexHttp.HexHttpServerManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType.CLIENT
import net.fabricmc.api.Environment

@Environment(CLIENT)
object HexServerClient {
    fun init() {
        HexHttpServerManager.init()

    }
}
