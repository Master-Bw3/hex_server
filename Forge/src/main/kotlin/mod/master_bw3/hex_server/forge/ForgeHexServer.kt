package mod.master_bw3.hex_server.forge

import dev.architectury.platform.forge.EventBuses
import mod.master_bw3.hex_server.HexServer
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataProvider.Factory
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

/**
 * This is your loading entrypoint on forge, in case you need to initialize
 * something platform-specific.
 */
@Mod(HexServer.MODID)
class HexServerForge {
    init {
        MOD_BUS.apply {
            EventBuses.registerModEventBus(HexServer.MODID, this)
            addListener(ForgeHexServerClient::init)
            addListener(::gatherData)
        }
        HexServer.init()
    }

    private fun gatherData(event: GatherDataEvent) {
        event.apply {
            val efh = existingFileHelper
        }
    }
}

fun <T : DataProvider> GatherDataEvent.addProvider(run: Boolean, factory: (DataOutput) -> T) =
    generator.addProvider(run, Factory { factory(it) })
