@file:JvmName("HexServerAbstractionsImpl")

package mod.master_bw3.hex_server.fabric

import mod.master_bw3.hex_server.registry.HexServerRegistrar
import net.minecraft.registry.Registry

fun <T : Any> initRegistry(registrar: HexServerRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
