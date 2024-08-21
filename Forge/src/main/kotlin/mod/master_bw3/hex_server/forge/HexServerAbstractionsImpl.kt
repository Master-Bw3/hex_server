@file:JvmName("HexServerAbstractionsImpl")

package mod.master_bw3.hex_server.forge

import mod.master_bw3.hex_server.registry.HexServerRegistrar
import net.minecraftforge.registries.RegisterEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

fun <T : Any> initRegistry(registrar: HexServerRegistrar<T>) {
    MOD_BUS.addListener { event: RegisterEvent ->
        event.register(registrar.registryKey) { helper ->
            registrar.init(helper::register)
        }
    }
}
