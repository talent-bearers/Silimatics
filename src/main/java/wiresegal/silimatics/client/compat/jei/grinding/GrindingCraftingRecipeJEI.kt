package wiresegal.silimatics.client.compat.jei.grinding

import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.item.ItemStack
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType

class GrindingCraftingRecipeJEI(val enumSandType: EnumSandType) : BlankRecipeWrapper() {

    override fun getInputs(): List<Any> {
        return listOf(ItemStack(ModBlocks.glassPane, 1, enumSandType.ordinal))
    }

    override fun getOutputs(): List<Any> {
        return listOf(ItemStack(ModItems.lens, 1, enumSandType.ordinal))
    }

}
