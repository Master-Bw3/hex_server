@file:OptIn(ExperimentalStdlibApi::class)

package mod.master_bw3.hex_server.forge.datagen

import mod.master_bw3.hex_server.HexServer
import mod.master_bw3.hex_server.items.ItemDebugger
import mod.master_bw3.hex_server.items.ItemDebugger.DebugState
import mod.master_bw3.hex_server.items.ItemDebugger.StepMode
import mod.master_bw3.hex_server.items.ItemEvaluator
import mod.master_bw3.hex_server.items.ItemEvaluator.EvalState
import mod.master_bw3.hex_server.registry.HexServerItems
import mod.master_bw3.hex_server.utils.itemPredicate
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.client.model.generators.ModelBuilder
import net.minecraftforge.client.model.generators.ModelFile
import net.minecraftforge.common.data.ExistingFileHelper

class HexServerModels(output: PackOutput, efh: ExistingFileHelper) : ItemModelProvider(output, HexServer.MODID, efh) {
    override fun registerModels() {
        basicItem(HexServerItems.DUMMY_ITEM.id)
            .parent(ModelFile.UncheckedModelFile("item/handheld_rod"))
    }
}

// utility function for adding multiple possibly missing layers to a generated item model
fun <T : ModelBuilder<T>> T.layers(start: Int, vararg layers: String?): T {
    var index = start
    for (layer in layers) {
        if (layer != null) {
            texture("layer$index", layer)
            index += 1
        }
    }
    return this
}
