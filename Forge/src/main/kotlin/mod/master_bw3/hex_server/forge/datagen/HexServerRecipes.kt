package mod.master_bw3.hex_server.forge.datagen

import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.paucal.api.datagen.PaucalRecipeProvider
import mod.master_bw3.hex_server.HexServer
import mod.master_bw3.hex_server.registry.HexServerItems
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Items
import java.util.function.Consumer

// we use Paucal's recipe provider as a base because it has a bunch of helpful stuff
class HexServerRecipes(output: PackOutput) : PaucalRecipeProvider(output, HexServer.MODID) {
    override fun buildRecipes(writer: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HexServerItems.DUMMY_ITEM.value)
            .define('S', Items.STICK)
            .define('A', Blocks.AMETHYST_BLOCK)
            .pattern("  A")
            .pattern(" S ")
            .pattern("S  ")
            .unlockedBy("has_item", hasItem(HexTags.Items.STAVES))
            .save(writer)
    }
}
