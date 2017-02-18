package wiresegal.silimatics.common.recipes

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import wiresegal.silimatics.common.block.BlockCymaticPlate
import wiresegal.silimatics.common.util.Vec2f

/**
 * Created by Elad on 10/12/2016.
 */
object ModCymaticPlateRecipes {
    fun registerCymaticPlateRecipe(rl: ResourceLocation, vec2f: Vec2f, itemStack: ItemStack) {
        BlockCymaticPlate.CymaticPlateRecipes.register(BlockCymaticPlate.CymaticPlateRecipe(Vec2f(1f, 2f), ItemStack(Items.DIAMOND)))
    }
}