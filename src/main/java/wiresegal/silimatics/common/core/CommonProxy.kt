package wiresegal.silimatics.common.core

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.silimatics.common.block.BlockSand
import wiresegal.silimatics.common.networking.NetworkHelper
import wiresegal.silimatics.common.potions.ModPotions
import wiresegal.silimatics.common.recipes.ModCymaticPlateRecipes
import wiresegal.silimatics.common.recipes.ModRecipes

open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        ModItems
        ModBlocks
        NetworkHelper
        ModCreativeTab
        ModPotions
        ModSoundEvents
        ModCymaticPlateRecipes
        FMLInterModComms.sendMessage("Waila", "register", "wiresegal.silimatics.client.compat.waila.WailaCompat.onWailaCall")
    }

    open fun init(event: FMLInitializationEvent) {
        ModRecipes
        BlockSand.DispenserBehaviourPlaceBrightsand
    }

    open fun postInit(event: FMLPostInitializationEvent) {

    }
}
