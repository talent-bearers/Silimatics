package wiresegal.silimatics.common.recipes

import net.minecraft.block.BlockPlanks
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.ShapedOreRecipe
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
            addRecipe(ItemStack(ModBlocks.sand, 1, sand.ordinal),
                    "SS",
                    "SS",
                    'S', ItemStack(ModItems.sand, 1, sand.ordinal))
            addRecipe(ItemStack(ModBlocks.glassPane, 2, sand.ordinal),
                    "GG",
                    "GG",
                    'G', ItemStack(ModBlocks.glass, 1, sand.ordinal))
            addRecipe(ItemStack(ModItems.frame).setLensStack(ItemStack(ModItems.lens, 1, sand.ordinal)),
                    "LIL",
                    "I I",
                    'L', ItemStack(ModItems.lens, 1, sand.ordinal),
                    'I', "ingotIron")

            GameRegistry.addSmelting(ItemStack(ModBlocks.sand, 1, sand.ordinal), ItemStack(ModBlocks.glass, 1, sand.ordinal), 0.1F)
        }

        for (wood in BlockPlanks.EnumType.values())
            addRecipe(ItemStack(ModBlocks.sifter, 1, wood.metadata),
                    "WSW",
                    "T T",
                    'W', ItemStack(Blocks.PLANKS, 1, wood.metadata),
                    'S', "string",
                    'T', "stickWood")

        addRecipe(ItemStack(ModBlocks.lensGrinder),
                "SIS",
                "S S",
                "SIS",
                'S', "stone",
                'I', ItemStack(Items.IRON_SWORD))

        addRecipe(ItemStack(ModItems.boots),
                "G G",
                "G G",
                'G', ItemStack(ModBlocks.glass, 1, EnumSandType.TRAIL.ordinal))
    }

    fun addRecipe(output: ItemStack, vararg inputs: Any) {
        GameRegistry.addRecipe(ShapedOreRecipe(output, *inputs))
    }
}
