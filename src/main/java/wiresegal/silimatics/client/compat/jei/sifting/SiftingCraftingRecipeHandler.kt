package wiresegal.silimatics.client.compat.jei.sifting

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import wiresegal.silimatics.common.lib.LibMisc

object SiftingCraftingRecipeHandler : IRecipeHandler<SiftingCraftingRecipeJEI> {
    override fun getRecipeClass(): Class<SiftingCraftingRecipeJEI> {
        return SiftingCraftingRecipeJEI::class.java
    }

    override fun getRecipeCategoryUid(): String {
        return "${LibMisc.MODID}:sifting"
    }

    override fun getRecipeWrapper(recipe: SiftingCraftingRecipeJEI): IRecipeWrapper {
        return recipe
    }

    override fun isRecipeValid(recipe: SiftingCraftingRecipeJEI): Boolean {
        return true
    }
}
