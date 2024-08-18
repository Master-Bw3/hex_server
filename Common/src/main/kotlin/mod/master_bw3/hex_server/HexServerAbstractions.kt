@file:JvmName("HexUIAbstractions")

package mod.master_bw3.hex_server

import dev.architectury.injectables.annotations.ExpectPlatform
import mod.master_bw3.hex_server.registry.HexServerRegistrar

fun initRegistries(vararg registries: HexServerRegistrar<out Any>) {
    for (registry in registries) {
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: HexServerRegistrar<T>) {
    throw AssertionError()
}
