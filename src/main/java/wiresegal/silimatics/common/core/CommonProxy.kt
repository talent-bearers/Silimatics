package wiresegal.silimatics.common.core

import net.minecraft.world.World
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.silimatics.common.recipes.ModRecipes
import wiresegal.silimatics.networking.NetworkHelper

open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        ModItems
        ModBlocks
        NetworkHelper
        ModCreativeTab
        FMLInterModComms.sendMessage("Waila", "register", "wiresegal.silimatics.common.compat.waila.WailaCompat.onWailaCall")
    }

    open fun init(event: FMLInitializationEvent) {
        ModRecipes
    }

    open fun postInit(event: FMLPostInitializationEvent) {

    }
}
