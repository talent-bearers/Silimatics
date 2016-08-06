package wiresegal.silimatics.client.compat.jei

import mezz.jei.api.BlankModPlugin
import mezz.jei.api.IJeiHelpers
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import net.minecraft.block.BlockPlanks
import net.minecraft.item.ItemStack
import wiresegal.silimatics.client.compat.jei.grinding.GrindingCraftingCategory
import wiresegal.silimatics.client.compat.jei.grinding.GrindingCraftingRecipeHandler
import wiresegal.silimatics.client.compat.jei.grinding.GrindingCraftingRecipeJEI
import wiresegal.silimatics.client.compat.jei.sifting.*
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType

/**
 * @author WireSegal
 * Created at 10:06 AM on 3/21/16.
 */
@JEIPlugin
class JEICompat : BlankModPlugin() {

    companion object {
        lateinit var helper: IJeiHelpers
    }

    override fun register(registry: IModRegistry) {
        helper = registry.jeiHelpers

        registry.addRecipeCategories(SiftingCraftingCategory, GrindingCraftingCategory)
        registry.addRecipeHandlers(SiftingCraftingRecipeHandler, GrindingCraftingRecipeHandler)
        registry.addRecipes(Array(EnumSandType.values().size) { SiftingCraftingRecipeJEI(EnumSandType.values()[it]) }.toList())
        registry.addRecipes(Array(EnumSandType.values().size) { GrindingCraftingRecipeJEI(EnumSandType.values()[it]) }.toList())

        for (i in BlockPlanks.EnumType.values())
            registry.addRecipeCategoryCraftingItem(ItemStack(ModBlocks.sifter, 1, i.metadata), SiftingCraftingCategory.uid)
        registry.addRecipeCategoryCraftingItem(ItemStack(ModBlocks.lensGrinder), GrindingCraftingCategory.uid)
    }
}
