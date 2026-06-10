package mod.master_bw3.hex_server.client

import mod.master_bw3.hex_server.HexServerClient
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod


@Mod(value = "hex_server", dist = [Dist.CLIENT])
class NeoForgeHexServerClient(modBus: IEventBus, container: ModContainer, dist: Dist) {
    init {
        HexServerClient.init()
    }
}