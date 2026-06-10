package mod.master_bw3.hex_server

import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

@Mod(NeoForgeHexServer.ID)
class NeoForgeHexServer(modBus: IEventBus, container: ModContainer) {
    init {
        HexServer.init()
        modBus.register(ModEvents)
    }

    companion object {
        const val ID = "hex_server"
    }
}

object ModEvents {
    @SubscribeEvent // on the mod event bus
    fun registerPayloadHandlersEvent(event: RegisterPayloadHandlersEvent) {
        // Sets the current network version
        val registrar = event.registrar("1")
        NeoForgePacketHandler.init(registrar)
    }

}