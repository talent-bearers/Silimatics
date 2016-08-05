package wiresegal.silimatics.common.recipes

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.item.ItemLensFrames.Companion.setLensStack

/**
 * @author WireSegal
 * Created at 10:45 PM on 8/4/16.
 */
object ModRecipes {
    init {
        for (sand in EnumSandType.values()) {
            GameRegistry.addRecipe(ShapedOreRecipe(ItemStack(ModBlocks.sand, 1, sand.ordinal),
                    "SS",
                    "SS",
                    'S', ItemStack(ModItems.sand, 1, sand.ordinal)))
            GameRegistry.addRecipe(ShapedOreRecipe(ItemStack(ModBlocks.glassPane, 2, sand.ordinal),
                    "GG",
                    "GG",
                    'G', ItemStack(ModBlocks.glass, 1, sand.ordinal)))
            GameRegistry.addRecipe(ShapedOreRecipe(ItemStack(ModItems.frame).setLensStack(ItemStack(ModItems.lens, 1, sand.ordinal)),
                    "LIL",
                    "I I",
                    'L', ItemStack(ModItems.lens, 1, sand.ordinal),
                    'I', "ingotIron"))

            GameRegistry.addSmelting(ItemStack(ModBlocks.sand, 1, sand.ordinal), ItemStack(ModBlocks.glass, 1, sand.ordinal), 0.1F)
        }
    }
}
