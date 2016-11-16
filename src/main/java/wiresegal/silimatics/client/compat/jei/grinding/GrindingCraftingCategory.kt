package wiresegal.silimatics.client.compat.jei.grinding

import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import wiresegal.silimatics.client.compat.jei.JEICompat
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.lib.LibMisc

object GrindingCraftingCategory : IRecipeCategory {

    private val background = JEICompat.helper.guiHelper.createDrawable(ResourceLocation(LibMisc.MODID, "textures/gui/jei/grinding.png"), 0, 0, 76, 44)

    override fun getUid(): String {
        return "${LibMisc.MODID}:grinding"
    }

    override fun getTitle(): String {
        return I18n.format("jei.${LibMisc.MODID}.recipe.grinding")
    }

    override fun getBackground(): IDrawable {
        return background
    }

    override fun drawExtras(minecraft: Minecraft) {

    }

    override fun drawAnimations(minecraft: Minecraft) {

    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper) {

        recipeLayout.itemStacks.init(PANE_SLOT, true, 0, 13)
        recipeLayout.itemStacks.init(GRINDER_SLOT, false, 29, 13)
        recipeLayout.itemStacks.init(OUTPUT_SLOT, false, 58, 13)

        if (recipeWrapper is GrindingCraftingRecipeJEI) {
            recipeLayout.itemStacks.set(PANE_SLOT, recipeWrapper.inputs[0] as ItemStack)
            recipeLayout.itemStacks.set(GRINDER_SLOT, ItemStack(ModBlocks.lensGrinder))
            recipeLayout.itemStacks.set(OUTPUT_SLOT, recipeWrapper.outputs[0] as ItemStack)
        }
    }

    private val PANE_SLOT = 0
    private val GRINDER_SLOT = 1
    private val OUTPUT_SLOT = 2
}
