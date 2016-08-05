package wiresegal.silimatics.common.compat.jei

import mezz.jei.api.*
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.lib.LibMisc

@JEIPlugin
class JeiCompat : IModPlugin {
    companion object {
        var jeiHelpers: IJeiHelpers? = null
    }
    override fun register(registry: IModRegistry) {
        jeiHelpers = registry.jeiHelpers
        registry.addRecipeCategories(SieveRecipeCatagory())
        registry.addRecipeHandlers(SieveRecipeHandler())
        for(i in 0..EnumSandType.values().size)
            registry.addRecipes(mutableListOf(ItemStack(ModItems.sand, 1, i)))
        registry.addRecipeCategoryCraftingItem(ModBlocks.sifter(), SieveRecipeCatagory.uid0)
    }

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {

    }


}
    class SieveRecipeCatagory : IRecipeCategory {
        companion object {
            val uid0 = "${LibMisc.MODID}:sieve" //uid0 so kotlin doesn't loop to getUid
        }
        override fun getUid(): String {
            return uid0
        }

        override fun drawAnimations(minecraft: Minecraft) {

        }

        override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper) {
            //recipeLayout.itemStacks
        }

        override fun getBackground(): IDrawable {
            return object : IDrawable {
                override fun getHeight(): Int = 128

                override fun draw(minecraft: Minecraft) {
                    draw(minecraft, 0, 0)
                }

                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    val renderItem = minecraft.renderItem
                    renderItem.renderItemIntoGUI(ModBlocks.sifter(), xOffset, yOffset)
                }

                override fun getWidth(): Int = 128
            }
        }


        override fun drawExtras(minecraft: Minecraft) {

        }

        override fun getTitle(): String {
            return "${LibMisc.MODID}.sieve"
        }

    }
class SieveRecipeHandler : IRecipeHandler<ItemStack> {
    override fun isRecipeValid(recipe: ItemStack): Boolean {
        return recipe.item == Item.getItemFromBlock(Blocks.SAND)
    }

    override fun getRecipeWrapper(recipe: ItemStack): IRecipeWrapper {
        return SieveRecipeWrapper(recipe.metadata)
    }

    override fun getRecipeClass(): Class<ItemStack> {
        return ItemStack::class.java
    }

    override fun getRecipeCategoryUid(): String {
        return SieveRecipeCatagory.uid0
    }

}
class SieveRecipeWrapper : IRecipeWrapper {
    val id: Int
    constructor(id: Int) {
        this.id = id
    }
    override fun drawAnimations(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int) {
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): MutableList<String>? {
        return null
    }

    override fun getFluidInputs(): MutableList<FluidStack>? {
        return null
    }

    override fun handleClick(minecraft: Minecraft, mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        return true
    }

    override fun getOutputs(): MutableList<Any?>? {
        return mutableListOf(ItemStack(ModItems.sand, 1, id))
    }

    override fun getFluidOutputs(): MutableList<FluidStack>? {
        return null
    }

    override fun getInputs(): MutableList<Any?>? {
        return mutableListOf(ItemStack(Blocks.SAND))
    }
}
