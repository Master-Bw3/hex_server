package mod.master_bw3.hex_server.client.HexHttp

import dev.architectury.event.events.client.ClientPlayerEvent

object HexHttpServerManager {
    const val PORT = 9000

    private var server: HexHttpServer? = null

    val hexRequestHandler: MCServerEvalHexRequestHandler?
        get() = server?.hexRequestHandler

    fun init() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register { player ->
            server?.stop()
            server = HexHttpServer(player, PORT)
        }

        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register {
            server?.stop()
            server = null
        }
    }
}