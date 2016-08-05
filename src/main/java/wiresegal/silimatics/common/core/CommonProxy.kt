package wiresegal.silimatics.common.core

import net.minecraft.world.storage.loot.LootTableList
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.silimatics.common.recipes.ModRecipes

open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        ModItems
        ModBlocks
        LootTableList.register(ModBlocks.sifter.lootTable)
    }

    fun init(event: FMLInitializationEvent) {
        ModRecipes
    }

    fun postInit(event: FMLPostInitializationEvent) {

    }
}
