package wiresegal.silimatics.client.compat.jei.grinding

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import wiresegal.silimatics.common.lib.LibMisc

object GrindingCraftingRecipeHandler : IRecipeHandler<GrindingCraftingRecipeJEI> {
    override fun getRecipeClass(): Class<GrindingCraftingRecipeJEI> {
        return GrindingCraftingRecipeJEI::class.java
    }

    override fun getRecipeCategoryUid(): String {
        return "${LibMisc.MODID}:grinding"
    }

    override fun getRecipeWrapper(recipe: GrindingCraftingRecipeJEI): IRecipeWrapper {
        return recipe
    }

    override fun isRecipeValid(recipe: GrindingCraftingRecipeJEI): Boolean {
        return true
    }
}
