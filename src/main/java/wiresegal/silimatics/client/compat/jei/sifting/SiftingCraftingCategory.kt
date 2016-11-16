package wiresegal.silimatics.client.compat.jei.sifting

import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.block.BlockPlanks
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import wiresegal.silimatics.client.compat.jei.JEICompat
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.lib.LibMisc

object SiftingCraftingCategory : IRecipeCategory<SiftingCraftingRecipeJEI> {

    private val background = JEICompat.helper.guiHelper.createDrawable(ResourceLocation(LibMisc.MODID, "textures/gui/jei/sifting.png"), 0, 0, 87, 37)

    override fun getUid(): String {
        return "${LibMisc.MODID}:sifting"
    }

    override fun getTitle(): String {
        return I18n.format("jei.${LibMisc.MODID}.recipe.sifting")
    }

    override fun getBackground(): IDrawable {
        return background
    }

    override fun drawExtras(minecraft: Minecraft) {

    }

    override fun drawAnimations(minecraft: Minecraft) {

    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: SiftingCraftingRecipeJEI) {

        recipeLayout.itemStacks.init(SAND_SLOT, true, 6, 1)
        recipeLayout.itemStacks.init(SIFTER_SLOT, false, 6, 18)
        recipeLayout.itemStacks.init(OUTPUT_SLOT, false, 60, 10)

        if (recipeWrapper is SiftingCraftingRecipeJEI) {
            recipeLayout.itemStacks.set(SAND_SLOT, ItemStack(Blocks.SAND))
            recipeLayout.itemStacks.set(SIFTER_SLOT, sifterStacks)
            recipeLayout.itemStacks.set(OUTPUT_SLOT, recipeWrapper.outputs[0] as ItemStack)
        }
    }

    val sifterStacks by lazy {
        Array(BlockPlanks.EnumType.values().size) {
            ItemStack(ModBlocks.sifter, 1, it)
        }.toList()
    }

    private val SAND_SLOT = 0
    private val SIFTER_SLOT = 1
    private val OUTPUT_SLOT = 2
}
