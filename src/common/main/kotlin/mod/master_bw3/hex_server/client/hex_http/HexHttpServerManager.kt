package mod.master_bw3.hex_server.client.hex_http

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

object HexHttpServerManager {
    const val PORT = 9000

    private var server: HexHttpServer? = null

    val hexRequestHandler: EvalHexRequestHandler?
        get() = server?.hexRequestHandler

    fun init() {
        ClientPlayConnectionEvents.JOIN.register { packetListenter, packetSender, minecraft  ->
            server?.stop()
            server = HexHttpServer(minecraft.player!!, PORT)
        }

        ClientPlayConnectionEvents.DISCONNECT.register { packetListenter, minecraft ->
            server?.stop()
            server = null
        }
    }
}