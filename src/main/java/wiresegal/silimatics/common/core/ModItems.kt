package wiresegal.silimatics.common.core

import net.minecraft.item.ItemArmor
import wiresegal.silimatics.common.block.BlockCymaticPlate
import wiresegal.silimatics.common.item.*
import wiresegal.silimatics.common.lib.LibNames
import wiresegal.zenmodelloader.common.ZenModelLoader

object ModItems {
    val lens: ItemLens
    val frame: ItemLensFrames
    val sand: ItemSand
    val boots: ItemGrapplerBoots
    val fork: BlockCymaticPlate.ItemTuningFork
    val shard: ItemCommunicatorShard
    lateinit var debugger: ItemDebugger

    init {
        lens = ItemLens(LibNames.LENS)
        frame = ItemLensFrames(LibNames.LENS_FRAMES, ItemArmor.ArmorMaterial.LEATHER)
        sand = ItemSand(LibNames.SAND_ITEM)
        boots = ItemGrapplerBoots(LibNames.GRAPPLER)
        fork = BlockCymaticPlate.ItemTuningFork()
        shard = ItemCommunicatorShard()
        if (ZenModelLoader.DEV_ENVIRONMENT && ItemDebugger.shouldRegisterInDevEnv) debugger = ItemDebugger(LibNames.DEBUGGER)
    }



}
