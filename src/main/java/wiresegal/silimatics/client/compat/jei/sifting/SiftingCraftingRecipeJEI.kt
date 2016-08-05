package wiresegal.silimatics.client.compat.jei.sifting

import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType

class SiftingCraftingRecipeJEI(val enumSandType: EnumSandType) : BlankRecipeWrapper() {

    override fun getInputs(): List<Any> {
        return listOf(ItemStack(Blocks.SAND))
    }

    override fun getOutputs(): List<Any> {
        return listOf(ItemStack(ModItems.sand, 1, enumSandType.ordinal))
    }

}
