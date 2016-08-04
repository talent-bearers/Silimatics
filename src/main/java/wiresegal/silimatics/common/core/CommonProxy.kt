package wiresegal.silimatics.common.core

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        ModItems
        ModBlocks
    }

    fun init(event: FMLInitializationEvent) {

    }

    fun postInit(event: FMLPostInitializationEvent) {

    }
}
