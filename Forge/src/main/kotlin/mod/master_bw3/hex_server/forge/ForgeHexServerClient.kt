package mod.master_bw3.hex_server.forge

import mod.master_bw3.hex_server.HexServerClient
//import mod.master_bw3.hex_server.config.HexServerConfig
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT

object ForgeHexServerClient {
    fun init(event: FMLClientSetupEvent) {
        HexServerClient.init()
//        LOADING_CONTEXT.registerExtensionPoint(ConfigScreenFactory::class.java) {
//            ConfigScreenFactory { _, parent -> HexServerConfig.getConfigScreen(parent) }
//        }
    }
}
