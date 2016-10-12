package wiresegal.silimatics.common.recipes

import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import wiresegal.silimatics.common.block.BlockCymaticPlate
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.silimatics.common.util.Vec2f

/**
 * Created by Elad on 10/12/2016.
 */
object ModCymaticPlateRecipes {
    init {
        registerCymaticPlateRecipe(ResourceLocation(LibMisc.MODID, "test"), Vec2f(0.1f, 0.1f), ItemStack(Blocks.STONE))
    }
    fun registerCymaticPlateRecipe(rl: ResourceLocation, vec2f: Vec2f, itemStack: ItemStack) {
        BlockCymaticPlate.CymaticPlateRecipes.register(BlockCymaticPlate.CymaticPlateRecipe(rl, vec2f, itemStack))
    }
}