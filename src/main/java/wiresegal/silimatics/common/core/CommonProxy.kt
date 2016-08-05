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
        FMLInterModComms.sendMessage("Waila", "register", "wiresegal.silimatics.common.compat.waila.WailaCompat.onWailaCall");
    }

    open fun init(event: FMLInitializationEvent) {
        ModRecipes
    }

    fun postInit(event: FMLPostInitializationEvent) {

    }

    open fun makeParticleDust(x: Double, y: Double, z: Double, xs: Double, ys: Double, zs: Double, id: Int) {
        //NO-OP
    }

    open fun makeParticleOculator(worldIn: World?, xCoordIn: Double, yCoordIn: Double, zCoordIn: Double, scale: Float, red: Float, green: Float, blue: Float) {
        //NO-OP
    }
}
